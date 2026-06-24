package ru.psb.masking.transformers.api;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder
public class TransformContext {
    String schema;
    String table;
    String column;
    int jdbcType;
    Map<String, String> params;
}
