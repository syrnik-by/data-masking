package ru.yourcompany.masking.it;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yourcompany.masking.dialect.postgres.PostgresDialect;
import ru.yourcompany.masking.test.PostgresTestContainer;
import ru.yourcompany.masking.test.SampleSchemaBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PostgresDialectIT {

    @Container
    static PostgresTestContainer postgres = PostgresTestContainer.create();

    @BeforeAll
    static void setup() throws Exception {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            SampleSchemaBuilder.create(conn);
            SampleSchemaBuilder.insertSampleData(conn);
        }
    }

    @Test
    void shouldReadSchema() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            var schema = dialect.readSchema(conn, "public");
            assertNotNull(schema);
            assertFalse(schema.tables().isEmpty(), "Expected at least one table");
            assertTrue(schema.tables().stream().anyMatch(t -> t.name().equals("customer")));
        }
    }
}
