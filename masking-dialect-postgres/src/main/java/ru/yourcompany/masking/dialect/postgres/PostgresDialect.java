package ru.yourcompany.masking.dialect.postgres;

import ru.yourcompany.masking.core.schema.*;
import ru.yourcompany.masking.dialect.api.DatabaseDialect;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PostgresDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "postgres"; }

    @Override
    public SchemaModel readSchema(Connection connection, String schema) {
        List<TableModel> tables = new ArrayList<>();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, schema, "%", new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(readTable(meta, connection.getCatalog(), schema, tableName));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read schema: " + schema, e);
        }
        return new SchemaModel(null, schema, tables);
    }

    private TableModel readTable(DatabaseMetaData meta, String catalog, String schema, String tableName) throws SQLException {
        List<ColumnModel> columns = new ArrayList<>();
        List<String> pks = new ArrayList<>();
        List<ForeignKeyModel> fks = new ArrayList<>();

        ResultSet cols = meta.getColumns(catalog, schema, tableName, "%");
        while (cols.next()) {
            columns.add(new ColumnModel(
                cols.getString("COLUMN_NAME"),
                cols.getString("TYPE_NAME"),
                cols.getString("IS_NULLABLE").equals("YES"),
                false,
                cols.getInt("COLUMN_SIZE")
            ));
        }

        ResultSet pkRs = meta.getPrimaryKeys(catalog, schema, tableName);
        while (pkRs.next()) pks.add(pkRs.getString("COLUMN_NAME"));

        ResultSet fkRs = meta.getImportedKeys(catalog, schema, tableName);
        while (fkRs.next()) {
            fks.add(new ForeignKeyModel(
                fkRs.getString("FK_NAME"),
                fkRs.getString("FKCOLUMN_NAME"),
                fkRs.getString("PKTABLE_NAME"),
                fkRs.getString("PKCOLUMN_NAME")
            ));
        }

        return new TableModel(tableName, columns, pks, fks);
    }

    @Override
    public Stream<List<Map<String, Object>>> readTable(Connection connection, TableRef table, int batchSize) {
        // TODO: implement cursor-based streaming with batching
        return Stream.empty();
    }

    @Override
    public void writeBatch(Connection connection, TableRef table, List<Map<String, Object>> batch, List<String> columns) {
        // TODO: implement batch UPSERT/INSERT
    }

    @Override
    public void disableConstraints(Connection connection, String schema) {
        // TODO: SET session_replication_role = replica;
    }

    @Override
    public void enableConstraints(Connection connection, String schema) {
        // TODO: SET session_replication_role = DEFAULT;
    }
}
