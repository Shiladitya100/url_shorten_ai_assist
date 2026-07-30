package com.schwab.urlshortener.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class Base62ShortCodeGenerator implements ShortCodeGenerator {

    public static final String BASE62_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom secureRandom;

    public Base62ShortCodeGenerator() {
        this(new SecureRandom());
    }

    Base62ShortCodeGenerator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public String generate(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Short code length must be greater than zero");
        }

        StringBuilder code = new StringBuilder(length);
        for (int index = 0; index < length; index++) {
            int characterIndex = secureRandom.nextInt(BASE62_ALPHABET.length());
            code.append(BASE62_ALPHABET.charAt(characterIndex));
        }
        return code.toString();
    }
}
