package ru.psb.masking.transformers;

import org.junit.jupiter.api.Test;
import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.deterministic.DeterministicFakeEmailTransformer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DeterministicFakeEmailTransformerTest {

    private final DeterministicFakeEmailTransformer transformer = new DeterministicFakeEmailTransformer();
    private final TransformContext ctx = TransformContext.builder()
            .schema("public").table("customers").column("email")
            .jdbcType(12).params(Map.of()).build();

    @Test
    void sameInputAlwaysProducesSameOutput() {
        Object first  = transformer.transform("ivan@mail.ru", ctx);
        Object second = transformer.transform("ivan@mail.ru", ctx);
        assertThat(first).isEqualTo(second);
    }

    @Test
    void differentInputsProduceDifferentOutputs() {
        Object a = transformer.transform("alice@mail.ru", ctx);
        Object b = transformer.transform("bob@mail.ru", ctx);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void outputEndsWithMaskedDomain() {
        String result = (String) transformer.transform("test@example.com", ctx);
        assertThat(result).endsWith("@masked.local");
    }

    @Test
    void nullInputReturnsNull() {
        assertThat(transformer.transform(null, ctx)).isNull();
    }

    @Test
    void nameIsCorrect() {
        assertThat(transformer.name()).isEqualTo("DETERMINISTIC_FAKE_EMAIL");
    }
}
