package com.schwab.urlshortener.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ShortCodeRulesTest {

    @Test
    void shouldExposeBase62PatternForConfiguredLength() {
        assertThat(ShortCodeRules.LENGTH).isEqualTo(7);
        assertThat("AbC123x").matches(ShortCodeRules.PATTERN);
        assertThat("abc-123").doesNotMatch(ShortCodeRules.PATTERN);
        assertThat("AbC123").doesNotMatch(ShortCodeRules.PATTERN);
        assertThat("AbC123xy").doesNotMatch(ShortCodeRules.PATTERN);
    }

    @Test
    void shouldIdentifyReservedCodesCaseInsensitively() {
        assertThat(ShortCodeRules.isReserved("actuator")).isTrue();
        assertThat(ShortCodeRules.isReserved("ACTUATOR")).isTrue();
        assertThat(ShortCodeRules.isReserved("AbC123x")).isFalse();
        assertThat(ShortCodeRules.isReserved(null)).isFalse();
    }
}
