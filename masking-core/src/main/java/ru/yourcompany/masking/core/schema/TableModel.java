package ru.yourcompany.masking.core.schema;

import java.util.List;

public record TableModel(
        String name,
        List<ColumnModel> columns,
        List<String> primaryKeys,
        List<ForeignKeyModel> foreignKeys
) {}
