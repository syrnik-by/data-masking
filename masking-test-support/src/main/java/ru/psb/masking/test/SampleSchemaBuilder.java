package ru.psb.masking.test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SampleSchemaBuilder {

    public static void build(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS customers (
                        id   SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(200),
                        phone VARCHAR(20)
                    )""");
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id          SERIAL PRIMARY KEY,
                        customer_id INT NOT NULL REFERENCES customers(id),
                        amount      NUMERIC(12,2),
                        note        TEXT
                    )""");
        }
    }
}
