package ru.psb.masking.api.rule;

public interface MaskingRule {
    String getColumnSelector();
    String getTransformerName();
}
