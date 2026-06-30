package ru.psb.masking.transformers;

import org.junit.jupiter.api.Test;
import ru.psb.masking.common.MaskingException;
import ru.psb.masking.transformers.deterministic.DeterministicFakeEmailTransformer;
import ru.psb.masking.transformers.registry.TransformerRegistry;
import ru.psb.masking.transformers.text.NullifyTransformer;

import static org.assertj.core.api.Assertions.*;

class TransformerRegistryTest {

    @Test
    void findRegisteredTransformer() {
        TransformerRegistry registry = new TransformerRegistry();
        registry.register(new NullifyTransformer());
        assertThat(registry.find("NULLIFY")).isInstanceOf(NullifyTransformer.class);
    }

    @Test
    void throwsOnUnknownTransformerName() {
        TransformerRegistry registry = new TransformerRegistry();
        registry.register(new DeterministicFakeEmailTransformer());
        assertThatThrownBy(() -> registry.find("DOES_NOT_EXIST"))
                .isInstanceOf(MaskingException.class)
                .hasMessageContaining("Unknown transformer");
    }

    @Test
    void registerOverwritesPreviousTransformer() {
        TransformerRegistry registry = new TransformerRegistry();
        NullifyTransformer t1 = new NullifyTransformer();
        NullifyTransformer t2 = new NullifyTransformer();
        registry.register(t1);
        registry.register(t2);
        assertThat(registry.find("NULLIFY")).isSameAs(t2);
    }
}
