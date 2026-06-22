package ru.yourcompany.masking.transformers.registry;

import ru.yourcompany.masking.common.exception.MaskingException;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.util.HashMap;
import java.util.Map;

public class TransformerRegistry {

    private final Map<String, Transformer> registry = new HashMap<>();

    public void register(Transformer transformer) {
        registry.put(transformer.code(), transformer);
    }

    public Transformer get(String code) {
        Transformer t = registry.get(code);
        if (t == null) throw new MaskingException("Unknown transformer: " + code);
        return t;
    }

    public boolean has(String code) {
        return registry.containsKey(code);
    }
}
