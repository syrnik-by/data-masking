package ru.psb.masking.core.schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ForeignKeyModel {
    String name;
    TableRef fkTable;
    String fkColumn;
    TableRef pkTable;
    String pkColumn;
}
