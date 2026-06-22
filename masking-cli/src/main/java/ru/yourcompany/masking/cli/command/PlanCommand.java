package ru.yourcompany.masking.cli.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import ru.yourcompany.masking.engine.service.MaskingService;

@Slf4j
@RequiredArgsConstructor
public class PlanCommand implements ApplicationRunner {

    private final MaskingService maskingService;

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("config")) {
            log.error("Usage: --config=<path to config.yml>");
            return;
        }
        String configPath = args.getOptionValues("config").get(0);
        var plan = maskingService.plan(configPath);
        log.info("Execution plan: {} tables in order: {}", plan.tableOrder().size(), plan.tableOrder());
    }
}
