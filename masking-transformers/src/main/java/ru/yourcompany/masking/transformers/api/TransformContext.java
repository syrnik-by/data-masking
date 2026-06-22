package ru.yourcompany.masking.transformers.api;

public record TransformContext(
        String table,
        String column,
        long rowIndex
) {}
