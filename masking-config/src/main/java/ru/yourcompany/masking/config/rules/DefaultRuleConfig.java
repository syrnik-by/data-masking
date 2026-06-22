package ru.yourcompany.masking.config.rules;

import lombok.Data;

@Data
public class DefaultRuleConfig {
    private String match;
    private String transformer;
}
