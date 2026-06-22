CREATE TABLE IF NOT EXISTS masking_run (
    run_id      UUID PRIMARY KEY,
    started_at  TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    status      VARCHAR(50) NOT NULL,
    config_path VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS masking_run_table (
    id               BIGSERIAL PRIMARY KEY,
    run_id           UUID NOT NULL REFERENCES masking_run(run_id),
    table_name       VARCHAR(255) NOT NULL,
    source_row_count BIGINT,
    target_row_count BIGINT,
    masked_columns   BIGINT
);

CREATE TABLE IF NOT EXISTS masking_validation_issue (
    id         BIGSERIAL PRIMARY KEY,
    run_id     UUID NOT NULL REFERENCES masking_run(run_id),
    table_name VARCHAR(255),
    column_name VARCHAR(255),
    severity   VARCHAR(20) NOT NULL,
    message    TEXT
);
