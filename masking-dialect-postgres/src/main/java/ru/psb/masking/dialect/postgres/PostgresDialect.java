package ru.psb.masking.dialect.postgres;

import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.core.schema.*;
import ru.psb.masking.dialect.api.DatabaseDialect;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PostgresDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "postgresql"; }

    // ── readSchema ────────────────────────────────────────────────────────────

    @Override
    public SchemaModel readSchema(DataSource dataSource, String schema) {
        List<TableModel> tables = new ArrayList<>();
        List<ForeignKeyModel> foreignKeys = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            String catalog = conn.getCatalog();

            try (ResultSet rs = meta.getTables(catalog, schema, null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    List<ColumnModel> columns = readColumns(meta, catalog, schema, tableName);
                    tables.add(TableModel.builder()
                            .schema(schema).name(tableName).columns(columns).build());
                }
            }

            for (TableModel table : tables) {
                try (ResultSet fkRs = meta.getImportedKeys(catalog, schema, table.getName())) {
                    while (fkRs.next()) {
                        foreignKeys.add(ForeignKeyModel.builder()
                                .name(fkRs.getString("FK_NAME"))
                                .fkTable(new TableRef(schema, fkRs.getString("FKTABLE_NAME")))
                                .fkColumn(fkRs.getString("FKCOLUMN_NAME"))
                                .pkTable(new TableRef(fkRs.getString("PKTABLE_SCHEM"), fkRs.getString("PKTABLE_NAME")))
                                .pkColumn(fkRs.getString("PKCOLUMN_NAME"))
                                .build());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read schema '" + schema + "'", e);
        }
        return SchemaModel.builder().schema(schema).tables(tables).foreignKeys(foreignKeys).build();
    }

    private List<ColumnModel> readColumns(DatabaseMetaData meta, String catalog,
                                          String schema, String table) throws SQLException {
        List<ColumnModel> columns = new ArrayList<>();
        Set<String> pkColumns = new LinkedHashSet<>();

        try (ResultSet pk = meta.getPrimaryKeys(catalog, schema, table)) {
            while (pk.next()) pkColumns.add(pk.getString("COLUMN_NAME"));
        }
        try (ResultSet cols = meta.getColumns(catalog, schema, table, null)) {
            while (cols.next()) {
                String colName = cols.getString("COLUMN_NAME");
                columns.add(ColumnModel.builder()
                        .name(colName)
                        .jdbcType(cols.getInt("DATA_TYPE"))
                        .typeName(cols.getString("TYPE_NAME"))
                        .nullable(cols.getInt("NULLABLE") == DatabaseMetaData.columnNullable)
                        .primaryKey(pkColumns.contains(colName))
                        .build());
            }
        }
        return columns;
    }

    // ── readTable ─────────────────────────────────────────────────────────────

    @Override
    public List<Map<String, Object>> readTable(DataSource dataSource, String schema, String table) {
        String sql = "SELECT * FROM " + qi(schema) + "." + qi(table);
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read table " + schema + "." + table, e);
        }

        log.debug("Read {} rows from {}.{}", rows.size(), schema, table);
        return rows;
    }

    // ── writeBatch ────────────────────────────────────────────────────────────

    @Override
    public void writeBatch(DataSource dataSource, String schema, String table,
                           List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            log.debug("writeBatch: no rows to write for {}.{}", schema, table);
            return;
        }

        String qualified = qi(schema) + "." + qi(table);
        List<String> columns = new ArrayList<>(rows.get(0).keySet());

        String colList      = columns.stream().map(this::qi).collect(Collectors.joining(", "));
        String placeholders = columns.stream().map(c -> "?").collect(Collectors.joining(", "));
        String insertSql    = "INSERT INTO " + qualified + " (" + colList + ") VALUES (" + placeholders + ")";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // TRUNCATE the target table before inserting masked data
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("TRUNCATE TABLE " + qualified + " CASCADE");
                }
                // Batch INSERT
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    for (Map<String, Object> row : rows) {
                        for (int i = 0; i < columns.size(); i++) {
                            ps.setObject(i + 1, row.get(columns.get(i)));
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                conn.commit();
                log.debug("Wrote {} rows to {}", rows.size(), qualified);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to write batch to " + schema + "." + table, e);
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /** Quotes a PostgreSQL identifier to handle reserved words and mixed case. */
    private String qi(String name) {
        return "\"" + name.replace("\"", "\"\"") + "\"";
    }
}
