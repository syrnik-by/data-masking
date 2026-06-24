package ru.psb.masking.dialect.api;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DialectRegistry {

    private static final Map<String, DatabaseDialect> DIALECTS = new ConcurrentHashMap<>();

    public static void register(DatabaseDialect dialect) {
        DIALECTS.put(dialect.dialectName().toLowerCase(), dialect);
    }

    public static Optional<DatabaseDialect> find(String name) {
        return Optional.ofNullable(DIALECTS.get(name.toLowerCase()));
    }
}
