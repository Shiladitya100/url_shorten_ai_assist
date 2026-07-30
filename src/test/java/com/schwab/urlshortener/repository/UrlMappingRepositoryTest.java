package com.schwab.urlshortener.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.schwab.urlshortener.entity.UrlMapping;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlMappingRepositoryTest {

    @Autowired
    private UrlMappingRepository repository;

    @Test
    void shouldPersistAndFindUrlMappingByShortCode() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/some/long/path")
                .shortCode("AbC123x")
                .createdAt(now)
                .expiresAt(now.plusDays(7))
                .build();

        repository.saveAndFlush(mapping);

        assertThat(repository.existsByShortCode("AbC123x")).isTrue();
        assertThat(repository.findByShortCode("AbC123x"))
                .isPresent()
                .get()
                .satisfies(saved -> {
                    assertThat(saved.getId()).isNotNull();
                    assertThat(saved.getOriginalUrl()).isEqualTo("https://example.com/some/long/path");
                    assertThat(saved.getShortCode()).isEqualTo("AbC123x");
                    assertThat(saved.getAccessCount()).isZero();
                    assertThat(saved.getLastAccessedAt()).isNull();
                    assertThat(saved.isActive()).isTrue();
                });
    }

    @Test
    void shouldRecordSuccessfulAccessAtomically() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        OffsetDateTime accessedAt = OffsetDateTime.parse("2026-07-30T10:05:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/some/long/path")
                .shortCode("AbC123x")
                .createdAt(now)
                .build();
        repository.saveAndFlush(mapping);

        int updatedRows = repository.recordSuccessfulAccess("AbC123x", accessedAt);

        assertThat(updatedRows).isEqualTo(1);
        assertThat(repository.findByShortCode("AbC123x"))
                .isPresent()
                .get()
                .satisfies(saved -> {
                    assertThat(saved.getAccessCount()).isEqualTo(1);
                    assertThat(saved.getLastAccessedAt()).isEqualTo(accessedAt);
                });
    }
}
