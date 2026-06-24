package ru.psb.masking.api.report;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ValidationIssue {
    String table;
    String column;
    IssueSeverity severity;
    String message;
}
