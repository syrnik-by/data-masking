package ru.psb.masking.engine.planning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.api.plan.ExecutionPlan;
import ru.psb.masking.api.plan.ExecutionPlan.ColumnPlan;
import ru.psb.masking.api.plan.ExecutionPlan.TablePlan;
import ru.psb.masking.config.MaskingConfig;
import ru.psb.masking.config.loader.YamlConfigLoader;
import ru.psb.masking.config.rules.RuleConfig;
import ru.psb.masking.config.rules.RulesetConfig;
import ru.psb.masking.core.schema.*;
import ru.psb.masking.dialect.api.DatabaseDialect;
import ru.psb.masking.dialect.api.DialectRegistry;
import ru.psb.masking.engine.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DefaultPlanningService implements PlanningService {

    private final YamlConfigLoader configLoader;
    private final DialectRegistry dialectRegistry;
    private final DataSourceFactory dataSourceFactory;

    @Override
    public ExecutionPlan buildPlan(String configPath) {
        log.info("Building plan from: {}", configPath);

        MaskingConfig config = configLoader.load(configPath);
        DatabaseDialect dialect = dialectRegistry.find(config.getSource().getDialect());
        DataSource ds = dataSourceFactory.create(config.getSource());

        String schema = config.getSource().getSchema();
        SchemaModel schemaModel = dialect.readSchema(ds, schema);
        log.info("Discovered {} tables in schema '{}'", schemaModel.getTables().size(), schema);

        // Topological sort — parent tables before child tables (FK order)
        List<TableRef> orderedRefs = TopologicalSorter.sort(
                schemaModel.getTables().stream()
                        .map(t -> new TableRef(t.getSchema(), t.getName()))
                        .collect(Collectors.toList()),
                schemaModel.getForeignKeys()
        );

        List<TablePlan> tablePlans = orderedRefs.stream()
                .map(ref -> buildTablePlan(ref, schemaModel, config.getRulesets()))
                .filter(tp -> !tp.getColumns().isEmpty()) // skip tables with no masking rules
                .collect(Collectors.toList());

        log.info("Plan built: {} tables will be masked", tablePlans.size());
        return ExecutionPlan.builder()
                .jobId(UUID.randomUUID().toString())
                .tables(tablePlans)
                .build();
    }

    // -------------------------------------------------------------------------

    private TablePlan buildTablePlan(TableRef ref, SchemaModel schemaModel,
                                     List<RulesetConfig> rulesets) {
        TableModel tableModel = schemaModel.findTable(ref);
        Optional<RulesetConfig> rulesetOpt = findRuleset(rulesets, ref);

        List<ColumnPlan> colPlans = tableModel.getColumns().stream()
                .filter(col -> !col.isPrimaryKey()) // never mask PK columns
                .map(col -> resolveColumnPlan(col, rulesetOpt.orElse(null)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return TablePlan.builder()
                .schema(ref.getSchema())
                .table(ref.getTable())
                .columns(colPlans)
                .build();
    }

    private Optional<RulesetConfig> findRuleset(List<RulesetConfig> rulesets, TableRef ref) {
        if (rulesets == null) return Optional.empty();
        return rulesets.stream()
                .filter(r -> ref.getTable().equalsIgnoreCase(r.getTable())
                        && (r.getSchema() == null || ref.getSchema().equalsIgnoreCase(r.getSchema())))
                .findFirst();
    }

    /**
     * Resolves which transformer to use for a column.
     * Priority: explicit rule > defaultRule > nothing (column skipped).
     */
    private Optional<ColumnPlan> resolveColumnPlan(ColumnModel col, RulesetConfig ruleset) {
        if (ruleset == null) return Optional.empty();

        // 1. Explicit per-column rule
        if (ruleset.getRules() != null) {
            Optional<RuleConfig> explicit = ruleset.getRules().stream()
                    .filter(r -> col.getName().equalsIgnoreCase(r.getColumn()))
                    .findFirst();
            if (explicit.isPresent()) {
                return Optional.of(ColumnPlan.builder()
                        .column(col.getName())
                        .transformerName(explicit.get().getTransformer())
                        .build());
            }
        }

        // 2. Default rule for the table
        if (ruleset.getDefaultRule() != null) {
            return Optional.of(ColumnPlan.builder()
                    .column(col.getName())
                    .transformerName(ruleset.getDefaultRule().getTransformer())
                    .build());
        }

        return Optional.empty();
    }
}
