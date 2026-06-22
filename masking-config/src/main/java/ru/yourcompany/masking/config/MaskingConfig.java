package ru.yourcompany.masking.config;

import lombok.Data;
import ru.yourcompany.masking.config.profile.ConnectionProfile;
import ru.yourcompany.masking.config.rules.RulesetConfig;

import java.util.Map;

@Data
public class MaskingConfig {
    private Map<String, ConnectionProfile> profiles;
    private RulesetConfig ruleset;
}
