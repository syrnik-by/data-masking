package ru.yourcompany.masking.core.policy;

import ru.yourcompany.masking.api.rule.MaskingRule;

import java.util.List;

public record Ruleset(
        List<MaskingRule> rules,
        List<DefaultMaskingRule> defaults
) {}
