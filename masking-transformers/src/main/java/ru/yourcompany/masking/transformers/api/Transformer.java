package ru.yourcompany.masking.transformers.api;

import java.util.Map;

public interface Transformer {
    String code();
    Object transform(Object value, Map<String, Object> params, TransformContext context);
    boolean isDeterministic();
}
