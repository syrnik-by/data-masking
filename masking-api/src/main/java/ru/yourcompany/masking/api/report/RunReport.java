package ru.yourcompany.masking.api.report;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RunReport(
        UUID runId,
        Instant startedAt,
        Instant finishedAt,
        RunStatus status,
        List<TableRunReport> tables,
        List<ValidationIssue> issues
) {}
