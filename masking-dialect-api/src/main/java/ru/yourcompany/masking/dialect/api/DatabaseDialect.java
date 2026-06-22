package ru.yourcompany.masking.dialect.api;

import ru.yourcompany.masking.core.schema.SchemaModel;
import ru.yourcompany.masking.core.schema.TableRef;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface DatabaseDialect {
    String dialectName();
    SchemaModel readSchema(Connection connection, String schema);
    Stream<List<Map<String, Object>>> readTable(Connection connection, TableRef table, int batchSize);
    void writeBatch(Connection connection, TableRef table, List<Map<String, Object>> batch, List<String> columns);
    void disableConstraints(Connection connection, String schema);
    void enableConstraints(Connection connection, String schema);
}
