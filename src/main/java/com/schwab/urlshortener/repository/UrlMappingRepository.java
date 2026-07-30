package com.schwab.urlshortener.repository;

import com.schwab.urlshortener.entity.UrlMapping;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update UrlMapping mapping
               set mapping.accessCount = mapping.accessCount + 1,
                   mapping.lastAccessedAt = :accessedAt
             where mapping.shortCode = :shortCode
            """)
    int recordSuccessfulAccess(String shortCode, OffsetDateTime accessedAt);
}
