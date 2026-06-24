package ru.psb.masking.dialect.oracle;

import ru.psb.masking.core.schema.SchemaModel;
import ru.psb.masking.dialect.api.DatabaseDialect;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class OracleDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "oracle"; }

    @Override
    public SchemaModel readSchema(DataSource dataSource, String schema) {
        throw new UnsupportedOperationException("TODO: implement Oracle schema reading");
    }

    @Override
    public List<Map<String, Object>> readTable(DataSource dataSource, String schema, String table) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void writeBatch(DataSource dataSource, String schema, String table,
                           List<Map<String, Object>> rows) {
        throw new UnsupportedOperationException("TODO");
    }
}
