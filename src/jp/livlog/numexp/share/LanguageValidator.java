package jp.livlog.numexp.share;

import java.util.Objects;

public final class LanguageValidator {

    private LanguageValidator() {
    }


    public static String requireSupported(String language) {

        Objects.requireNonNull(language, "language must not be null");
        if (!language.equals("ja") && !language.equals("zh")) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }
        return language;
    }
}
