package ru.psb.masking.cli.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.psb.masking.engine.planning.PlanningService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanCommand {

    private final PlanningService planningService;

    public void execute(String configPath) {
        log.info("Building execution plan from config: {}", configPath);
        var plan = planningService.buildPlan(configPath);
        log.info("Plan built. Tables to process: {}", plan.getTables().size());
    }
}
