package ru.psb.masking.api.report;

import lombok.Builder;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
@Builder
public class RunReport {
    String jobId;
    RunStatus status;
    Instant startedAt;
    Instant finishedAt;
    List<TableRunReport> tables;
    List<ValidationIssue> issues;
}
