package ru.yourcompany.masking.config.rules;

import lombok.Data;

import java.util.List;

@Data
public class RulesetConfig {
    private List<RuleConfig> rules;
    private List<DefaultRuleConfig> defaults;
}
