package ru.yourcompany.masking.dialect.mssql;

import ru.yourcompany.masking.core.schema.*;
import ru.yourcompany.masking.dialect.api.DatabaseDialect;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MssqlDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "mssql"; }

    @Override
    public SchemaModel readSchema(Connection connection, String schema) {
        // TODO: implement MSSQL schema introspection via INFORMATION_SCHEMA
        throw new UnsupportedOperationException("MSSQL dialect not yet implemented");
    }

    @Override
    public Stream<List<Map<String, Object>>> readTable(Connection connection, TableRef table, int batchSize) {
        // TODO: implement MSSQL cursor-based read with OFFSET/FETCH NEXT
        throw new UnsupportedOperationException("MSSQL dialect not yet implemented");
    }

    @Override
    public void writeBatch(Connection connection, TableRef table, List<Map<String, Object>> batch, List<String> columns) {
        // TODO: implement MSSQL batch write (MERGE or INSERT/UPDATE)
        throw new UnsupportedOperationException("MSSQL dialect not yet implemented");
    }

    @Override
    public void disableConstraints(Connection connection, String schema) {
        // TODO: ALTER TABLE ... NOCHECK CONSTRAINT ALL
    }

    @Override
    public void enableConstraints(Connection connection, String schema) {
        // TODO: ALTER TABLE ... WITH CHECK CHECK CONSTRAINT ALL
    }
}
