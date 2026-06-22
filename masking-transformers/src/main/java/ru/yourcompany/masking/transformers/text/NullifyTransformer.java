package ru.yourcompany.masking.transformers.text;

import ru.yourcompany.masking.transformers.api.TransformContext;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.util.Map;

public class NullifyTransformer implements Transformer {

    @Override
    public String code() { return "nullify"; }

    @Override
    public Object transform(Object value, Map<String, Object> params, TransformContext ctx) {
        return null;
    }

    @Override
    public boolean isDeterministic() { return true; }
}
