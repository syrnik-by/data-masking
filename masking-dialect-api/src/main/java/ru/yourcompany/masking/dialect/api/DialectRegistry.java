package ru.yourcompany.masking.dialect.api;

import ru.yourcompany.masking.common.exception.MaskingException;

import java.util.HashMap;
import java.util.Map;

public class DialectRegistry {

    private final Map<String, DatabaseDialect> dialects = new HashMap<>();

    public void register(DatabaseDialect dialect) {
        dialects.put(dialect.dialectName().toLowerCase(), dialect);
    }

    public DatabaseDialect get(String name) {
        DatabaseDialect d = dialects.get(name.toLowerCase());
        if (d == null) throw new MaskingException("Unknown dialect: " + name);
        return d;
    }
}
