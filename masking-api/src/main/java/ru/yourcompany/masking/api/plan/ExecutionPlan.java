package ru.yourcompany.masking.api.plan;

import ru.yourcompany.masking.api.rule.MaskingRule;

import java.util.List;

public record ExecutionPlan(
        List<String> tableOrder,
        java.util.Map<String, List<MaskingRule>> rulesByTable
) {}
