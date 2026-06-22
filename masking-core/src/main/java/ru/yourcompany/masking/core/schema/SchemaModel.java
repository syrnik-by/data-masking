package ru.yourcompany.masking.core.schema;

import java.util.List;

public record SchemaModel(
        String catalog,
        String schema,
        List<TableModel> tables
) {}
