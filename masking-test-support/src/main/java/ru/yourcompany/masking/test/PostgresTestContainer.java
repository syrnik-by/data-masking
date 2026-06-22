package ru.yourcompany.masking.test;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:16-alpine";

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer create() {
        return new PostgresTestContainer()
            .withDatabaseName("masking_test")
            .withUsername("test")
            .withPassword("test");
    }
}
