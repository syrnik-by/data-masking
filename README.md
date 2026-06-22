# data-masking

Open-source static data masking engine for PostgreSQL, MS SQL Server and Oracle.

## Modules

| Module | Description |
|---|---|
| masking-common | Shared utils, error model, base annotations |
| masking-api | Public contracts: MaskingJob, MaskingRule, ExecutionPlan, RunReport |
| masking-core | Domain model: schema, tables, columns, relations, policy |
| masking-config | YAML/properties config loading and validation |
| masking-transformers | Transformer library and registry |
| masking-engine | Orchestration: discovery → plan → extract → transform → load → validate → report |
| masking-dialect-api | SPI for database dialects |
| masking-dialect-postgres | PostgreSQL dialect implementation |
| masking-dialect-mssql | MS SQL Server dialect implementation |
| masking-dialect-oracle | Oracle dialect implementation |
| masking-audit | Audit schema, run report persistence |
| masking-cli | CLI commands: plan, preview, run, validate, report |
| masking-app | Spring Boot application entry point |
| masking-test-support | Shared test infra, Testcontainers fixtures, assertion helpers |
| masking-integration-tests | End-to-end integration tests |

## Quick start

```bash
./gradlew build
./gradlew :masking-cli:run --args="plan --config config/sample.yml"
```

## Requirements

- Java 17+
- Gradle 8+
- Docker (for integration tests via Testcontainers)
