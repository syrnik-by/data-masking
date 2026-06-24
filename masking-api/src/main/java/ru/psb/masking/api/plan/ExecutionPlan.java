package ru.psb.masking.api.plan;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class ExecutionPlan {
    String jobId;
    List<TablePlan> tables;

    @Value
    @Builder
    public static class TablePlan {
        String schema;
        String table;
        List<ColumnPlan> columns;
    }

    @Value
    @Builder
    public static class ColumnPlan {
        String column;
        String transformerName;
    }
}
