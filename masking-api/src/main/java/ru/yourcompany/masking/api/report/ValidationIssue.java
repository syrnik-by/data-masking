package ru.yourcompany.masking.api.report;

public record ValidationIssue(
        String table,
        String column,
        IssueSeverity severity,
        String message
) {}
