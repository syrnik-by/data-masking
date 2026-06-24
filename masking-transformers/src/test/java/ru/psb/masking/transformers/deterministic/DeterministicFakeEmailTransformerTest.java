package ru.psb.masking.transformers.deterministic;

import org.junit.jupiter.api.Test;
import ru.psb.masking.transformers.api.TransformContext;

import static org.assertj.core.api.Assertions.assertThat;

class DeterministicFakeEmailTransformerTest {

    private final DeterministicFakeEmailTransformer transformer = new DeterministicFakeEmailTransformer();

    @Test
    void transform_nullInput_returnsNull() {
        assertThat(transformer.transform(null, ctx())).isNull();
    }

    @Test
    void transform_sameInput_returnsSameOutput() {
        TransformContext ctx = ctx();
        Object r1 = transformer.transform("user@example.com", ctx);
        Object r2 = transformer.transform("user@example.com", ctx);
        assertThat(r1).isEqualTo(r2);
    }

    @Test
    void transform_differentInputs_returnDifferentOutputs() {
        Object r1 = transformer.transform("a@b.com", ctx());
        Object r2 = transformer.transform("c@d.com", ctx());
        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void transform_outputEndsWithMaskedLocal() {
        Object result = transformer.transform("test@real.com", ctx());
        assertThat(result.toString()).endsWith("@masked.local");
    }

    private TransformContext ctx() {
        return TransformContext.builder()
                .schema("public").table("users").column("email").jdbcType(12).build();
    }
}
