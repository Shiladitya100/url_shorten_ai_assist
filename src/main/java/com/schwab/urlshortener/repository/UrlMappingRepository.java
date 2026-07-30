package com.schwab.urlshortener.repository;

import com.schwab.urlshortener.entity.UrlMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}
