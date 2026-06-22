package ru.yourcompany.masking.transformers.text;

import ru.yourcompany.masking.common.util.StringUtils;
import ru.yourcompany.masking.transformers.api.TransformContext;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.util.Map;

public class PhoneMaskTransformer implements Transformer {

    @Override
    public String code() { return "phoneMask"; }

    @Override
    public Object transform(Object value, Map<String, Object> params, TransformContext ctx) {
        if (value == null) return null;
        return StringUtils.maskMiddle(value.toString(), '*', 2, 2);
    }

    @Override
    public boolean isDeterministic() { return true; }
}
