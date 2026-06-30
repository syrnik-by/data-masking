package ru.psb.masking.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.psb.masking.dialect.postgres.PostgresDialect;
import ru.psb.masking.test.PostgresTestContainer;
import ru.psb.masking.test.SampleSchemaBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class PostgresDialectReadWriteIT {

    private static final DataSource DS = PostgresTestContainer.dataSource();
    private final PostgresDialect dialect = new PostgresDialect();

    @BeforeEach
    void setUp() throws SQLException {
        SampleSchemaBuilder.build(DS);
        clearData();
        insertTestCustomers();
    }

    @Test
    void readTableReturnsAllRows() {
        List<Map<String, Object>> rows = dialect.readTable(DS, "public", "customers");
        assertThat(rows).hasSize(3);
    }

    @Test
    void readTableRowHasExpectedColumns() {
        List<Map<String, Object>> rows = dialect.readTable(DS, "public", "customers");
        Map<String, Object> row = rows.get(0);
        assertThat(row).containsKeys("id", "name", "email", "phone");
    }

    @Test
    void writeBatchReplacesAllRows() throws SQLException {
        List<Map<String, Object>> original = dialect.readTable(DS, "public", "customers");

        // mask email
        original.forEach(r -> r.put("email", "masked@masked.local"));

        dialect.writeBatch(DS, "public", "customers", original);

        List<Map<String, Object>> after = dialect.readTable(DS, "public", "customers");
        assertThat(after).hasSize(3);
        after.forEach(r -> assertThat(r.get("email")).isEqualTo("masked@masked.local"));
    }

    @Test
    void writeBatchWithEmptyListDoesNothing() {
        // must not throw
        dialect.writeBatch(DS, "public", "customers", List.of());
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void clearData() throws SQLException {
        try (Connection c = DS.getConnection();
             var st = c.createStatement()) {
            st.execute("TRUNCATE TABLE orders, customers RESTART IDENTITY CASCADE");
        }
    }

    private void insertTestCustomers() throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone) VALUES (?,?,?)";
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 3; i++) {
                ps.setString(1, "User" + i);
                ps.setString(2, "user" + i + "@mail.ru");
                ps.setString(3, "+7999000000" + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
