package ru.psb.masking.api.report;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TableRunReport {
    String schema;
    String table;
    long rowsProcessed;
    RunStatus status;
}
