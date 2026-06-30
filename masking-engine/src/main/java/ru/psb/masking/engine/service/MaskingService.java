package ru.psb.masking.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.api.plan.ExecutionPlan;
import ru.psb.masking.api.plan.ExecutionPlan.ColumnPlan;
import ru.psb.masking.api.plan.ExecutionPlan.TablePlan;
import ru.psb.masking.api.report.RunReport;
import ru.psb.masking.api.report.RunStatus;
import ru.psb.masking.api.report.TableRunReport;
import ru.psb.masking.config.MaskingConfig;
import ru.psb.masking.config.loader.YamlConfigLoader;
import ru.psb.masking.dialect.api.DatabaseDialect;
import ru.psb.masking.dialect.api.DialectRegistry;
import ru.psb.masking.engine.datasource.DataSourceFactory;
import ru.psb.masking.engine.planning.PlanningService;
import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;
import ru.psb.masking.transformers.registry.TransformerRegistry;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class MaskingService {

    private final PlanningService planningService;
    private final YamlConfigLoader configLoader;
    private final DialectRegistry dialectRegistry;
    private final DataSourceFactory dataSourceFactory;
    private final TransformerRegistry transformerRegistry;

    public RunReport run(String configPath) {
        Instant start = Instant.now();
        log.info("Starting masking job from config: {}", configPath);

        ExecutionPlan plan = planningService.buildPlan(configPath);
        MaskingConfig config = configLoader.load(configPath);

        DatabaseDialect dialect = dialectRegistry.find(config.getSource().getDialect());
        DataSource sourceDs = dataSourceFactory.create(config.getSource());
        DataSource targetDs = dataSourceFactory.create(config.getTarget());

        List<TableRunReport> tableReports = new ArrayList<>();

        for (TablePlan tablePlan : plan.getTables()) {
            TableRunReport tableReport = maskTable(tablePlan, dialect, sourceDs, targetDs);
            tableReports.add(tableReport);
        }

        RunStatus status = tableReports.stream()
                .anyMatch(r -> r.getStatus() == RunStatus.FAILED)
                ? RunStatus.FAILED : RunStatus.SUCCESS;

        log.info("Masking job finished with status={} tables={} elapsed={}ms",
                status, tableReports.size(), System.currentTimeMillis() - start.toEpochMilli());

        return RunReport.builder()
                .jobId(plan.getJobId())
                .status(status)
                .startedAt(start)
                .finishedAt(Instant.now())
                .tables(tableReports)
                .issues(List.of())
                .build();
    }

    // -------------------------------------------------------------------------

    private TableRunReport maskTable(TablePlan tablePlan,
                                     DatabaseDialect dialect,
                                     DataSource sourceDs,
                                     DataSource targetDs) {
        String schema = tablePlan.getSchema();
        String table  = tablePlan.getTable();
        log.info("Masking {}.{} ({} columns)", schema, table, tablePlan.getColumns().size());

        try {
            List<Map<String, Object>> rows = dialect.readTable(sourceDs, schema, table);
            List<Map<String, Object>> maskedRows = new ArrayList<>(rows.size());

            for (Map<String, Object> row : rows) {
                Map<String, Object> maskedRow = new LinkedHashMap<>(row);
                for (ColumnPlan colPlan : tablePlan.getColumns()) {
                    Object original = row.get(colPlan.getColumn());
                    Transformer transformer = transformerRegistry.find(colPlan.getTransformerName());
                    TransformContext ctx = TransformContext.builder()
                            .schema(schema)
                            .table(table)
                            .column(colPlan.getColumn())
                            .params(Map.of())
                            .build();
                    maskedRow.put(colPlan.getColumn(), transformer.transform(original, ctx));
                }
                maskedRows.add(maskedRow);
            }

            dialect.writeBatch(targetDs, schema, table, maskedRows);

            return TableRunReport.builder()
                    .schema(schema).table(table)
                    .rowsProcessed(rows.size())
                    .status(RunStatus.SUCCESS)
                    .build();

        } catch (Exception e) {
            log.error("Failed to mask {}.{}: {}", schema, table, e.getMessage(), e);
            return TableRunReport.builder()
                    .schema(schema).table(table)
                    .rowsProcessed(0)
                    .status(RunStatus.FAILED)
                    .build();
        }
    }
}
