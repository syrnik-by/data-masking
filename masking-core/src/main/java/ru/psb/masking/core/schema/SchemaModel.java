package ru.psb.masking.core.schema;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SchemaModel {
    String catalog;
    String schema;
    List<TableModel> tables;
    List<ForeignKeyModel> foreignKeys;

    /**
     * Finds a TableModel by its TableRef (schema + name).
     * Throws IllegalArgumentException if not found — signifies a config/schema mismatch.
     */
    public TableModel findTable(TableRef ref) {
        return tables.stream()
                .filter(t -> ref.getTable().equalsIgnoreCase(t.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Table not found in schema: " + ref.getSchema() + "." + ref.getTable()));
    }
}
