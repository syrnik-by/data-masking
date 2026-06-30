package ru.psb.masking.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.psb.masking.core.schema.SchemaModel;
import ru.psb.masking.dialect.postgres.PostgresDialect;
import ru.psb.masking.test.PostgresTestContainer;
import ru.psb.masking.test.SampleSchemaBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class PostgresDialectSchemaIT {

    private static final DataSource DS = PostgresTestContainer.dataSource();
    private final PostgresDialect dialect = new PostgresDialect();

    @BeforeEach
    void setUp() throws SQLException {
        SampleSchemaBuilder.build(DS);
    }

    @Test
    void readSchemaReturnsBothTables() {
        SchemaModel model = dialect.readSchema(DS, "public");
        assertThat(model.getTables())
                .extracting(t -> t.getName().toLowerCase())
                .contains("customers", "orders");
    }

    @Test
    void readSchemaDetectsForeignKey() {
        SchemaModel model = dialect.readSchema(DS, "public");
        assertThat(model.getForeignKeys()).isNotEmpty();
        assertThat(model.getForeignKeys().get(0).getPkTable().getTable().toLowerCase())
                .isEqualTo("customers");
    }

    @Test
    void customersTableHasPrimaryKey() {
        SchemaModel model = dialect.readSchema(DS, "public");
        var customers = model.findTable(new ru.psb.masking.core.schema.TableRef("public", "customers"));
        assertThat(customers.getColumns())
                .filteredOn(c -> c.getName().equals("id"))
                .allMatch(c -> c.isPrimaryKey());
    }

    @Test
    void emailColumnIsNullable() {
        SchemaModel model = dialect.readSchema(DS, "public");
        var customers = model.findTable(new ru.psb.masking.core.schema.TableRef("public", "customers"));
        assertThat(customers.getColumns())
                .filteredOn(c -> c.getName().equals("email"))
                .allMatch(c -> c.isNullable());
    }
}
