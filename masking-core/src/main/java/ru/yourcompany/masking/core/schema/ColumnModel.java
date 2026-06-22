package ru.yourcompany.masking.core.schema;

public record ColumnModel(
        String name,
        String jdbcType,
        boolean nullable,
        boolean unique,
        int maxLength
) {}
