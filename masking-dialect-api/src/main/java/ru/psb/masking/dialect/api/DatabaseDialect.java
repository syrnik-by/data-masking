package ru.psb.masking.dialect.api;

import ru.psb.masking.core.schema.SchemaModel;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface DatabaseDialect {
    String dialectName();
    SchemaModel readSchema(DataSource dataSource, String schema);
    List<Map<String, Object>> readTable(DataSource dataSource, String schema, String table);
    void writeBatch(DataSource dataSource, String schema, String table,
                    List<Map<String, Object>> rows);
}
