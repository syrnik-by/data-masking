package ru.yourcompany.masking.engine.planning;

import ru.yourcompany.masking.api.plan.ExecutionPlan;
import ru.yourcompany.masking.core.schema.SchemaModel;

public interface PlanningService {
    ExecutionPlan buildPlan(SchemaModel schema);
}
