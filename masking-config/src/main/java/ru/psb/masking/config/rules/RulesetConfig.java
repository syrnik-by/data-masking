package ru.psb.masking.config.rules;

import lombok.Data;
import java.util.List;

@Data
public class RulesetConfig {
    private String schema;
    private String table;
    private List<RuleConfig> rules;
    private DefaultRuleConfig defaultRule;
}
