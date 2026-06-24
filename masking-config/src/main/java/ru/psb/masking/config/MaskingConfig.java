package ru.psb.masking.config;

import lombok.Data;
import ru.psb.masking.config.profile.ConnectionProfile;
import ru.psb.masking.config.rules.RulesetConfig;

import java.util.List;

@Data
public class MaskingConfig {
    private ConnectionProfile source;
    private ConnectionProfile target;
    private List<RulesetConfig> rulesets;
}
