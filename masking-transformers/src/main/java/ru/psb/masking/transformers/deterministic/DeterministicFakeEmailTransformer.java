package ru.psb.masking.transformers.deterministic;

import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeterministicFakeEmailTransformer implements Transformer {

    @Override
    public String name() { return "DETERMINISTIC_FAKE_EMAIL"; }

    @Override
    public Object transform(Object value, TransformContext ctx) {
        if (value == null) return null;
        String hash = sha256Hex(value.toString()).substring(0, 12);
        return "user_" + hash + "@masked.local";
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
