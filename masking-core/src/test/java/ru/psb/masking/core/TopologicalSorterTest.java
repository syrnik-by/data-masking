package ru.psb.masking.core;

import org.junit.jupiter.api.Test;
import ru.psb.masking.core.schema.ForeignKeyModel;
import ru.psb.masking.core.schema.TableRef;
import ru.psb.masking.core.schema.TopologicalSorter;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TopologicalSorterTest {

    private static final String S = "public";

    @Test
    void parentBeforeChild() {
        // orders.customer_id -> customers.id
        List<TableRef> tables = List.of(ref("orders"), ref("customers"));
        List<ForeignKeyModel> fks = List.of(
                fk("orders", "customer_id", "customers", "id")
        );
        List<TableRef> sorted = TopologicalSorter.sort(tables, fks);
        assertThat(sorted.indexOf(ref("customers"))).isLessThan(sorted.indexOf(ref("orders")));
    }

    @Test
    void noForeignKeys_preservesOriginalOrder() {
        List<TableRef> tables = List.of(ref("aaa"), ref("bbb"), ref("ccc"));
        List<TableRef> sorted = TopologicalSorter.sort(tables, List.of());
        assertThat(sorted).containsExactlyElementsOf(tables);
    }

    @Test
    void multipleIndependentTables() {
        List<TableRef> tables = List.of(ref("a"), ref("b"), ref("c"));
        List<TableRef> sorted = TopologicalSorter.sort(tables, List.of());
        assertThat(sorted).hasSize(3);
    }

    @Test
    void chainOfDependencies() {
        // c -> b -> a
        List<TableRef> tables = List.of(ref("c"), ref("b"), ref("a"));
        List<ForeignKeyModel> fks = List.of(
                fk("c", "bid", "b", "id"),
                fk("b", "aid", "a", "id")
        );
        List<TableRef> sorted = TopologicalSorter.sort(tables, fks);
        int ai = sorted.indexOf(ref("a"));
        int bi = sorted.indexOf(ref("b"));
        int ci = sorted.indexOf(ref("c"));
        assertThat(ai).isLessThan(bi);
        assertThat(bi).isLessThan(ci);
    }

    // helpers
    private static TableRef ref(String table) { return new TableRef(S, table); }

    private static ForeignKeyModel fk(String fkTable, String fkCol,
                                      String pkTable, String pkCol) {
        return ForeignKeyModel.builder()
                .name(fkTable + "_" + fkCol + "_fk")
                .fkTable(ref(fkTable)).fkColumn(fkCol)
                .pkTable(ref(pkTable)).pkColumn(pkCol)
                .build();
    }
}
