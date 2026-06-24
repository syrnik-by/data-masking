package ru.psb.masking.core.policy;

import lombok.Builder;
import lombok.Value;
import ru.psb.masking.api.rule.MaskingRule;

import java.util.List;

@Value
@Builder
public class Ruleset {
    String schema;
    String table;
    List<MaskingRule> rules;
    MaskingRule defaultRule;
}
