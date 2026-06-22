package ru.yourcompany.masking.engine.planning;

import ru.yourcompany.masking.api.plan.ExecutionPlan;
import ru.yourcompany.masking.api.rule.MaskingRule;
import ru.yourcompany.masking.core.planning.TopologicalSorter;
import ru.yourcompany.masking.core.schema.SchemaModel;

import java.util.List;
import java.util.Map;

public class DefaultPlanningService implements PlanningService {

    private final TopologicalSorter sorter = new TopologicalSorter();

    @Override
    public ExecutionPlan buildPlan(SchemaModel schema) {
        List<String> tableOrder = sorter.sort(schema.tables());
        // TODO: enrich with rules from ruleset
        Map<String, List<MaskingRule>> rulesByTable = Map.of();
        return new ExecutionPlan(tableOrder, rulesByTable);
    }
}
