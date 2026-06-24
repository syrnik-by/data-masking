package ru.psb.masking.api.job;

import ru.psb.masking.api.plan.ExecutionPlan;
import ru.psb.masking.api.report.RunReport;

public interface MaskingJob {
    ExecutionPlan plan();
    RunReport execute();
}
