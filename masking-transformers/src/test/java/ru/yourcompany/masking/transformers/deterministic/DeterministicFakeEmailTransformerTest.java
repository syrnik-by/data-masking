package ru.yourcompany.masking.transformers.deterministic;

import org.junit.jupiter.api.Test;
import ru.yourcompany.masking.transformers.api.TransformContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicFakeEmailTransformerTest {

    private final DeterministicFakeEmailTransformer transformer = new DeterministicFakeEmailTransformer();

    @Test
    void shouldReturnNullForNullInput() {
        assertNull(transformer.transform(null, Map.of(), new TransformContext("customer", "email", 0)));
    }

    @Test
    void shouldReturnValidEmailFormat() {
        Object result = transformer.transform("test@mail.ru", Map.of(), new TransformContext("customer", "email", 0));
        assertNotNull(result);
        assertTrue(result.toString().contains("@"));
    }

    @Test
    void shouldBeDeterministic() {
        var ctx = new TransformContext("customer", "email", 0);
        Object r1 = transformer.transform("ivanov@test.ru", Map.of(), ctx);
        Object r2 = transformer.transform("ivanov@test.ru", Map.of(), ctx);
        assertEquals(r1, r2);
    }

    @Test
    void differentInputsShouldProduceDifferentOutputs() {
        var ctx = new TransformContext("customer", "email", 0);
        Object r1 = transformer.transform("ivanov@test.ru", Map.of(), ctx);
        Object r2 = transformer.transform("petrov@test.ru", Map.of(), ctx);
        assertNotEquals(r1, r2);
    }
}
