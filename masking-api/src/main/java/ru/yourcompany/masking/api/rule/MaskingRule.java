package ru.yourcompany.masking.api.rule;

public record MaskingRule(
        String table,
        String column,
        String transformer,
        java.util.Map<String, Object> params
) {}
