package ru.psb.masking.transformers.text;

import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;

public class PhoneMaskTransformer implements Transformer {

    @Override
    public String name() { return "PHONE_MASK"; }

    @Override
    public Object transform(Object value, TransformContext ctx) {
        if (value == null) return null;
        String phone = value.toString().replaceAll("[^\\d+]", "");
        if (phone.length() < 4) return "***";
        return phone.substring(0, phone.startsWith("+") ? 2 : 1)
                + "***"
                + phone.substring(phone.length() - 2);
    }
}
