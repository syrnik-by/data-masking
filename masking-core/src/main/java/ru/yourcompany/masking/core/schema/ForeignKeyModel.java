package ru.yourcompany.masking.core.schema;

public record ForeignKeyModel(
        String constraintName,
        String column,
        String referencedTable,
        String referencedColumn
) {}
