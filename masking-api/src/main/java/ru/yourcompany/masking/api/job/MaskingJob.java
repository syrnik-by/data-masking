package ru.yourcompany.masking.api.job;

import java.util.UUID;

public record MaskingJob(
        UUID id,
        String sourceProfile,
        String targetProfile,
        String rulesetPath
) {}
