package ru.psb.masking.transformers.text;

import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;

public class NullifyTransformer implements Transformer {

    @Override
    public String name() { return "NULLIFY"; }

    @Override
    public Object transform(Object value, TransformContext ctx) {
        return null;
    }
}
