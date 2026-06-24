package ru.psb.masking.dialect.mssql;

import ru.psb.masking.core.schema.SchemaModel;
import ru.psb.masking.dialect.api.DatabaseDialect;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class MssqlDialect implements DatabaseDialect {

    @Override
    public String dialectName() { return "mssql"; }

    @Override
    public SchemaModel readSchema(DataSource dataSource, String schema) {
        throw new UnsupportedOperationException("TODO: implement MSSQL schema reading");
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
