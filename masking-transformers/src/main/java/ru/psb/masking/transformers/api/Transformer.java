package ru.psb.masking.transformers.api;

public interface Transformer {
    String name();
    Object transform(Object value, TransformContext ctx);
}
