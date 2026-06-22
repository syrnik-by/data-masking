package ru.yourcompany.masking.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates a sample schema with customer, payment_card, orders tables for testing.
 */
public class SampleSchemaBuilder {

    public static void create(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customer (
                    id        BIGSERIAL PRIMARY KEY,
                    email     VARCHAR(255),
                    phone     VARCHAR(50),
                    full_name VARCHAR(255)
                )""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payment_card (
                    id          BIGSERIAL PRIMARY KEY,
                    customer_id BIGINT REFERENCES customer(id),
                    pan         VARCHAR(20)
                )""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id               BIGSERIAL PRIMARY KEY,
                    customer_id      BIGINT REFERENCES customer(id),
                    delivery_address TEXT,
                    total_amount     NUMERIC(12,2)
                )""");
        }
    }

    public static void insertSampleData(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                INSERT INTO customer (email, phone, full_name) VALUES
                    ('ivanov@example.com', '+7 900 111-22-33', 'Иванов Иван Иванович'),
                    ('petrov@test.ru',    '+7 900 444-55-66', 'Петров Пётр Петрович')
                """);

            stmt.execute("""
                INSERT INTO payment_card (customer_id, pan) VALUES
                    (1, '4111111111111111'),
                    (2, '5500005555555559')
                """);

            stmt.execute("""
                INSERT INTO orders (customer_id, delivery_address, total_amount) VALUES
                    (1, 'г. Ярославль, ул. Ленина 1', 1500.00),
                    (2, 'г. Москва, ул. Тверская 10', 3000.00)
                """);
        }
    }
}
