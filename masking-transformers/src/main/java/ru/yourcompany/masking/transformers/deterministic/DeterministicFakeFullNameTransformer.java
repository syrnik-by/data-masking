package ru.yourcompany.masking.transformers.deterministic;

import ru.yourcompany.masking.transformers.api.TransformContext;
import ru.yourcompany.masking.transformers.api.Transformer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

public class DeterministicFakeFullNameTransformer implements Transformer {

    private static final String[] FIRST = {
        "Alex", "Sam", "Jordan", "Taylor", "Morgan",
        "Riley", "Casey", "Avery", "Quinn", "Logan"
    };
    private static final String[] LAST = {
        "Smith", "Jones", "Brown", "Taylor", "Wilson",
        "Davis", "Evans", "Thomas", "Roberts", "Walker"
    };

    @Override
    public String code() { return "deterministicFakeFullName"; }

    @Override
    public Object transform(Object value, Map<String, Object> params, TransformContext ctx) {
        if (value == null) return null;
        String hash = sha256Hex(value.toString());
        int firstIdx = Math.abs((int) Long.parseUnsignedLong(hash.substring(0, 8), 16)) % FIRST.length;
        int lastIdx = Math.abs((int) Long.parseUnsignedLong(hash.substring(8, 16), 16)) % LAST.length;
        return FIRST[firstIdx] + " " + LAST[lastIdx];
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
