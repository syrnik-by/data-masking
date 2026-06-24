package ru.psb.masking.core.schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ColumnModel {
    String name;
    int jdbcType;
    String typeName;
    boolean nullable;
    boolean primaryKey;
}
