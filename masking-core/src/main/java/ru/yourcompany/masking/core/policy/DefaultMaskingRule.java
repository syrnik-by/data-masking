package ru.yourcompany.masking.core.policy;

public record DefaultMaskingRule(
        String columnPattern,
        String transformer
) {}
