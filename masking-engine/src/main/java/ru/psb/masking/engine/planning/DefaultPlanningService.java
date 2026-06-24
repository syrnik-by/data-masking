package ru.psb.masking.engine.planning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.api.plan.ExecutionPlan;

@Slf4j
@RequiredArgsConstructor
public class DefaultPlanningService implements PlanningService {

    @Override
    public ExecutionPlan buildPlan(String configPath) {
        log.info("Building plan from: {}", configPath);
        // TODO: load config, discover schema, apply rulesets, build ExecutionPlan
        return ExecutionPlan.builder()
                .jobId(java.util.UUID.randomUUID().toString())
                .tables(java.util.List.of())
                .build();
    }
}
