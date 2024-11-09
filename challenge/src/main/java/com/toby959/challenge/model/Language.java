package com.toby959.challenge.model;

public enum Language {
    ESPAÑOL("es", "español"),
    INGLES("en", "ingles"),
    FRANCES("fr", "frances"),
    PORTUGUES("pt", "portugues"),
    LATIN("la", "latin"),
    ALEMAN("de", "aleman"),
    ITALIANO("it", "italiano");

    private String langLiter;
    private String languageLiter;

    Language(String langLiter, String languageLiter) {
        this.langLiter = langLiter;
        this.languageLiter = languageLiter;
    }

    public static Language fromString(String text) {
        return getLanguageByProperty(text, true);
    }

    public static Language fromTotalString(String text) {
        return getLanguageByProperty(text, false);
    }

    private static Language getLanguageByProperty(String text, boolean isLangLiter) {
        for (Language language : Language.values()) {
            String property = isLangLiter ? language.langLiter : language.languageLiter;
            if (property.equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Ningún lenguaje encontrado: " + text);
    }
}
