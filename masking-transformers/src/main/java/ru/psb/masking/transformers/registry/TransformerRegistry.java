package ru.psb.masking.transformers.registry;

import ru.psb.masking.common.MaskingException;
import ru.psb.masking.transformers.api.Transformer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry of all available {@link Transformer} implementations.
 * Transformers are registered by name at application startup.
 */
public class TransformerRegistry {

    private final ConcurrentHashMap<String, Transformer> registry = new ConcurrentHashMap<>();

    public void register(Transformer transformer) {
        registry.put(transformer.name(), transformer);
    }

    public Transformer find(String name) {
        Transformer t = registry.get(name);
        if (t == null) {
            throw new MaskingException("Unknown transformer: '" + name
                    + "'. Registered transformers: " + registry.keySet());
        }
        return t;
    }

    public boolean contains(String name) {
        return registry.containsKey(name);
    }
}
