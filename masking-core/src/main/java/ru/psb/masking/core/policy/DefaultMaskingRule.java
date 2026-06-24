package ru.psb.masking.core.policy;

import lombok.Builder;
import lombok.Value;
import ru.psb.masking.api.rule.MaskingRule;

@Value
@Builder
public class DefaultMaskingRule implements MaskingRule {
    String columnSelector;
    String transformerName;
}
