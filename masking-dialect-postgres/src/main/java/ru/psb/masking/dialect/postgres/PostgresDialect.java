package ru.psb.masking.dialect.postgres;

import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.core.schema.*;
import ru.psb.masking.dialect.api.DatabaseDialect;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Slf4j
public class PostgresDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "postgresql"; }

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

    @Override
    public List<Map<String, Object>> readTable(DataSource dataSource, String schema, String table) {
        throw new UnsupportedOperationException("TODO: implement PostgreSQL readTable");
    }

    @Override
    public void writeBatch(DataSource dataSource, String schema, String table,
                           List<Map<String, Object>> rows) {
        throw new UnsupportedOperationException("TODO: implement PostgreSQL writeBatch");
    }
}
