package ru.yourcompany.masking.transformers.text;

import ru.yourcompany.masking.common.util.StringUtils;
import ru.yourcompany.masking.transformers.api.TransformContext;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.util.Map;

public class MaskMiddleTransformer implements Transformer {

    @Override
    public String code() { return "maskMiddle"; }

    @Override
    public Object transform(Object value, Map<String, Object> params, TransformContext ctx) {
        if (value == null) return null;
        String str = value.toString();
        int visibleLeft = intParam(params, "visibleLeft", 2);
        int visibleRight = intParam(params, "visibleRight", 2);
        char maskChar = charParam(params, "maskChar", '*');
        return StringUtils.maskMiddle(str, maskChar, visibleLeft, visibleRight);
    }

    @Override
    public boolean isDeterministic() { return true; }

    private int intParam(Map<String, Object> params, String key, int defaultValue) {
        if (params == null || !params.containsKey(key)) return defaultValue;
        return Integer.parseInt(params.get(key).toString());
    }

    private char charParam(Map<String, Object> params, String key, char defaultValue) {
        if (params == null || !params.containsKey(key)) return defaultValue;
        String val = params.get(key).toString();
        return val.isEmpty() ? defaultValue : val.charAt(0);
    }
}
