package ru.yourcompany.masking.core.schema;

public record TableRef(String schema, String table) {

    public static TableRef of(String schema, String table) {
        return new TableRef(schema, table);
    }

    @Override
    public String toString() {
        return schema != null ? schema + "." + table : table;
    }
}
