package org.chiclepad.backend.business;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class for working with ChiclePad Locales
 */
public class LocaleUtils {

    /**
     * List of all supported locales
     */
    private static final String[] availableLocales = {"sq-AL", "ar-DZ", "ar-BH", "ar-EG", "ar-IQ", "ar-JO", "ar-KW",
            "ar-LB", "ar-LY", "ar-MA", "ar-OM", "ar-QA", "ar-SA", "ar-SD", "ar-SY", "ar-TN", "ar-AE", "ar-YE", "be-BY",
            "bg-BG", "ca-ES", "zh-CN", "zh-SG", "zh-HK", "zh-TW", "hr-HR", "cs-CZ", "da-DK", "nl-BE", "nl-NL", "en-AU",
            "en-CA", "en-IN", "en-IE", "en-MT", "en-NZ", "en-PH", "en-SG", "en-ZA", "en-GB", "en-US", "et-EE", "fi-FI",
            "fr-BE", "fr-CA", "fr-FR", "fr-LU", "fr-CH", "de-AT", "de-DE", "de-LU", "de-CH", "el-CY", "el-GR", "hi-IN",
            "hu-HU", "is-IS", "ga-IE", "it-IT", "it-CH", "ja-JP", "ko-KR", "lv-LV", "lt-LT", "mk-MK", "ms-MY", "mt-MT",
            "no-NO", "nb-NO", "nn-NO", "no-NO-x-lvariant-NY", "pl-PL", "pt-BR", "pt-PT", "ro-RO", "ru-RU", "sr-BA",
            "sr-ME", "sr-RS", "sr-Latn-BA", "sr-Latn-ME", "sr-Latn-RS", "sk-SK", "sl-SI", "es-AR", "es-BO", "es-CL",
            "es-CO", "es-CR", "es-DO", "es-EC", "es-SV", "es-GT", "es-HN", "es-MX", "es-NI", "es-PA", "es-PY", "es-PE",
            "es-PR", "es-ES", "es-US", "es-UY", "es-VE", "sv-SE", "th-TH", "tr-TR", "uk-UA", "vi-VN"};


    /**
     * @return Locales supported by ChiclePad
     */
    public static List<Locale> getAllLocals() {
        return Arrays.stream(availableLocales)
                .map(Locale::forLanguageTag)
                .collect(Collectors.toList());
    }

    /**
     * @return Locales supported by ChiclePad as Strings
     */
    public static List<String> getAllLocalsAsStrings() {
        return List.of(availableLocales);
    }

    /**
     * @return All of format "English (Canada)"
     */
    public static List<String> getReadableLocales() {
        return Arrays.stream(availableLocales)
                .map(LocaleUtils::localeName)
                .collect(Collectors.toList());
    }

    /**
     * @return "English (Canada)" -> "en-Ca"
     */
    public static String getCodeFromReadableLocale(String readableLocale) {
        List<String> readableLocales = getReadableLocales();
        for (int i = 0; i < readableLocales.size(); i++) {
            if (readableLocales.get(i).equals(readableLocale)) {
                return availableLocales[i];
            }
        }
        return null;
    }

    /**
     * @return LocaleObject -> "English (Canada)"
     */
    public static String localeName(Locale locale) {
        return locale.getDisplayName();
    }

    /**
     * @return "en-CA" -> "English (Canada)"
     */
    public static String localeName(String code) {
        return localeName(Locale.forLanguageTag(code));
    }

    /**
     * @return "en-CA" -> LocaleObject
     */
    public static Locale localeFromCode(String code) {
        return Locale.forLanguageTag(code);
    }

    /**
     * @return LocaleObject -> "en-CA"
     */
    public static Optional<String> codeFromLocale(Locale locale) {
        if (locale == null) {
            return Optional.empty();
        }

        return getAllLocals().stream()
                .map(Locale::toLanguageTag)
                .filter(code -> code.equals(locale.toLanguageTag()))
                .findFirst();
    }

}
