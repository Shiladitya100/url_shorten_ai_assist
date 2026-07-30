package com.schwab.urlshortener.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class Base62ShortCodeGeneratorTest {

    private final Base62ShortCodeGenerator generator = new Base62ShortCodeGenerator();

    @Test
    void shouldGenerateCodeWithRequestedLengthAndBase62Characters() {
        String code = generator.generate(7);

        assertThat(code)
                .hasSize(7)
                .matches("[A-Za-z0-9]+");
    }

    @Test
    void shouldRejectNonPositiveLength() {
        assertThatThrownBy(() -> generator.generate(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Short code length must be greater than zero");
    }
}
