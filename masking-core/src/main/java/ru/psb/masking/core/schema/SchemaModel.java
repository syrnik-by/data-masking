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
}
