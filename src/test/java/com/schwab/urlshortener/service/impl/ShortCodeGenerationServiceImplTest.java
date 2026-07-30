package com.schwab.urlshortener.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.schwab.urlshortener.exception.ShortCodeGenerationException;
import com.schwab.urlshortener.repository.UrlMappingRepository;
import com.schwab.urlshortener.util.ShortCodeGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShortCodeGenerationServiceImplTest {

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private ShortCodeGenerationServiceImpl service;

    @Test
    void shouldReturnCandidateWhenUnique() {
        when(shortCodeGenerator.generate(7)).thenReturn("AbC123x");
        when(urlMappingRepository.existsByShortCode("AbC123x")).thenReturn(false);

        String code = service.generateUniqueCode();

        assertThat(code).isEqualTo("AbC123x");
        verify(urlMappingRepository).existsByShortCode("AbC123x");
    }

    @Test
    void shouldRetryWhenCandidateAlreadyExists() {
        when(shortCodeGenerator.generate(7)).thenReturn("AbC123x", "XyZ987q");
        when(urlMappingRepository.existsByShortCode("AbC123x")).thenReturn(true);
        when(urlMappingRepository.existsByShortCode("XyZ987q")).thenReturn(false);

        String code = service.generateUniqueCode();

        assertThat(code).isEqualTo("XyZ987q");
    }

    @Test
    void shouldRetryWhenCandidateIsReserved() {
        when(shortCodeGenerator.generate(7)).thenReturn("actuator", "XyZ987q");
        when(urlMappingRepository.existsByShortCode("XyZ987q")).thenReturn(false);

        String code = service.generateUniqueCode();

        assertThat(code).isEqualTo("XyZ987q");
    }

    @Test
    void shouldFailAfterMaximumAttempts() {
        when(shortCodeGenerator.generate(7)).thenReturn("AbC123x");
        when(urlMappingRepository.existsByShortCode("AbC123x")).thenReturn(true);

        assertThatThrownBy(() -> service.generateUniqueCode())
                .isInstanceOf(ShortCodeGenerationException.class)
                .hasMessage("Unable to generate a unique short code after 10 attempts");
    }
}
