package ru.psb.masking.it;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.psb.masking.core.schema.SchemaModel;
import ru.psb.masking.dialect.postgres.PostgresDialect;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class PostgresDialectIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @Test
    void readSchema_returnsSchemaModel() {
        var ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(postgres.getJdbcUrl());
        ds.setUsername(postgres.getUsername());
        ds.setPassword(postgres.getPassword());

        SchemaModel schema = new PostgresDialect().readSchema(ds, "public");
        assertThat(schema).isNotNull();
        assertThat(schema.getSchema()).isEqualTo("public");
    }
}
