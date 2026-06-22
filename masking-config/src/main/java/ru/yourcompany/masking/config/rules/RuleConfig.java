package ru.yourcompany.masking.config.rules;

import lombok.Data;

import java.util.Map;

@Data
public class RuleConfig {
    private String table;
    private String column;
    private String transformer;
    private Map<String, Object> params;
}
