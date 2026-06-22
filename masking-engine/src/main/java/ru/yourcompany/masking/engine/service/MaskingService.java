package ru.yourcompany.masking.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yourcompany.masking.api.plan.ExecutionPlan;
import ru.yourcompany.masking.api.report.RunReport;
import ru.yourcompany.masking.api.report.RunStatus;
import ru.yourcompany.masking.engine.discovery.SchemaDiscoveryService;
import ru.yourcompany.masking.engine.planning.PlanningService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MaskingService {

    private final SchemaDiscoveryService discoveryService;
    private final PlanningService planningService;

    public ExecutionPlan plan(String configPath) {
        log.info("Building execution plan from config: {}", configPath);
        var schema = discoveryService.discover(configPath);
        return planningService.buildPlan(schema);
    }

    public RunReport run(String configPath) {
        UUID runId = UUID.randomUUID();
        Instant start = Instant.now();
        log.info("Starting masking run [{}] with config: {}", runId, configPath);

        // TODO: wire full pipeline — extract, transform, load, validate, report

        return new RunReport(runId, start, Instant.now(), RunStatus.SUCCESS, List.of(), List.of());
    }
}
