package ru.psb.masking.transformers.registry;

import ru.psb.masking.transformers.api.Transformer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TransformerRegistry {

    private final Map<String, Transformer> transformers = new ConcurrentHashMap<>();

    public void register(Transformer transformer) {
        transformers.put(transformer.name().toUpperCase(), transformer);
    }

    public Optional<Transformer> find(String name) {
        return Optional.ofNullable(transformers.get(name.toUpperCase()));
    }

    public Map<String, Transformer> all() {
        return java.util.Collections.unmodifiableMap(transformers);
    }
}
