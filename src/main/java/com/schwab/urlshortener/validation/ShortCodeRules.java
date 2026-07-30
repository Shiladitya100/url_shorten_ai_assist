package com.schwab.urlshortener.validation;

import java.util.Set;

public final class ShortCodeRules {

    public static final int LENGTH = 7;
    public static final String PATTERN = "^[A-Za-z0-9]{" + LENGTH + "}$";
    public static final String VALIDATION_MESSAGE = "shortCode must be a 7-character Base62 value";

    private static final Set<String> RESERVED_CODES = Set.of(
            "actuator",
            "api",
            "error",
            "favicon",
            "h2-console",
            "swagger-ui",
            "v3"
    );

    private ShortCodeRules() {
    }

    public static boolean isReserved(String shortCode) {
        return shortCode != null && RESERVED_CODES.contains(shortCode.toLowerCase());
    }
}
