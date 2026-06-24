package ru.psb.masking.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.api.plan.ExecutionPlan;
import ru.psb.masking.api.report.RunReport;
import ru.psb.masking.api.report.RunStatus;
import ru.psb.masking.engine.planning.PlanningService;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MaskingService {

    private final PlanningService planningService;

    public RunReport run(String configPath) {
        Instant start = Instant.now();
        log.info("Starting masking job from config: {}", configPath);
        ExecutionPlan plan = planningService.buildPlan(configPath);
        // TODO: execute plan against dialect
        return RunReport.builder()
                .jobId(plan.getJobId())
                .status(RunStatus.SUCCESS)
                .startedAt(start)
                .finishedAt(Instant.now())
                .tables(List.of())
                .issues(List.of())
                .build();
    }
}
