package ru.yourcompany.masking.api.report;

public record TableRunReport(
        String table,
        long sourceRowCount,
        long targetRowCount,
        long maskedColumns
) {}
