package ru.yourcompany.masking.transformers.deterministic;

import ru.yourcompany.masking.transformers.api.TransformContext;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

/**
 * Deterministic email transformer.
 * Same input always produces the same output.
 * Uses SHA-256 hash of the original value as surrogate seed.
 */
public class DeterministicFakeEmailTransformer implements Transformer {

    private static final String[] DOMAINS = {
        "example.com", "masked.io", "test.local", "anon.dev", "fake.net"
    };

    @Override
    public String code() { return "deterministicFakeEmail"; }

    @Override
    public Object transform(Object value, Map<String, Object> params, TransformContext ctx) {
        if (value == null) return null;
        String hash = sha256Hex(value.toString());
        String local = hash.substring(0, 10);
        String domain = DOMAINS[Math.abs(hash.hashCode()) % DOMAINS.length];
        return local + "@" + domain;
    }

    @Override
    public boolean isDeterministic() { return true; }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
