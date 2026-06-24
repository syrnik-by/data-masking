package ru.psb.masking.config.rules;

import lombok.Data;

@Data
public class RuleConfig {
    private String column;
    private String transformer;
    private java.util.Map<String, String> params;
}
