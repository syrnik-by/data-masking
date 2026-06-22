package ru.yourcompany.masking.dialect.oracle;

import ru.yourcompany.masking.core.schema.*;
import ru.yourcompany.masking.dialect.api.DatabaseDialect;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OracleDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "oracle"; }

    @Override
    public SchemaModel readSchema(Connection connection, String schema) {
        // TODO: implement Oracle schema introspection via USER_TAB_COLUMNS / ALL_CONSTRAINTS
        throw new UnsupportedOperationException("Oracle dialect not yet implemented");
    }

    @Override
    public Stream<List<Map<String, Object>>> readTable(Connection connection, TableRef table, int batchSize) {
        // TODO: implement Oracle paging with OFFSET/FETCH (12c+) or ROWNUM
        throw new UnsupportedOperationException("Oracle dialect not yet implemented");
    }

    @Override
    public void writeBatch(Connection connection, TableRef table, List<Map<String, Object>> batch, List<String> columns) {
        // TODO: implement Oracle batch write (MERGE)
        throw new UnsupportedOperationException("Oracle dialect not yet implemented");
    }

    @Override
    public void disableConstraints(Connection connection, String schema) {
        // TODO: EXEC DBMS_UTILITY.exec_ddl_statement
    }

    @Override
    public void enableConstraints(Connection connection, String schema) {
        // TODO
    }
}
