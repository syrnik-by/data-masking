package ru.psb.masking.transformers.text;

import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;
import ru.psb.masking.common.util.StringUtils;

public class MaskMiddleTransformer implements Transformer {

    @Override
    public String name() { return "MASK_MIDDLE"; }

    @Override
    public Object transform(Object value, TransformContext ctx) {
        if (value == null) return null;
        int left  = parseInt(ctx.getParams(), "visibleLeft",  2);
        int right = parseInt(ctx.getParams(), "visibleRight", 2);
        return StringUtils.maskMiddle(value.toString(), left, right, '*');
    }

    private int parseInt(java.util.Map<String, String> params, String key, int def) {
        if (params == null || !params.containsKey(key)) return def;
        try { return Integer.parseInt(params.get(key)); } catch (NumberFormatException e) { return def; }
    }
}
