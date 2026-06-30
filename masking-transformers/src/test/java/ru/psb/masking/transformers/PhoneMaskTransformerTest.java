package ru.psb.masking.transformers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.text.PhoneMaskTransformer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneMaskTransformerTest {

    private final PhoneMaskTransformer transformer = new PhoneMaskTransformer();
    private final TransformContext ctx = TransformContext.builder()
            .schema("public").table("customers").column("phone")
            .jdbcType(12).params(Map.of()).build();

    @ParameterizedTest(name = "input={0}")
    @CsvSource({
            "+79991234567,  +7***67",
            "89991234567,   8***67",
            "+7 (999) 123-45-67, +7***67"
    })
    void masksPhoneCorrectly(String input, String expected) {
        assertThat(transformer.transform(input.trim(), ctx)).isEqualTo(expected.trim());
    }

    @Test
    void nullInputReturnsNull() {
        assertThat(transformer.transform(null, ctx)).isNull();
    }
}
