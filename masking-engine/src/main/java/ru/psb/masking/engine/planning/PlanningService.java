package ru.psb.masking.engine.planning;

import ru.psb.masking.api.plan.ExecutionPlan;

public interface PlanningService {
    ExecutionPlan buildPlan(String configPath);
}
