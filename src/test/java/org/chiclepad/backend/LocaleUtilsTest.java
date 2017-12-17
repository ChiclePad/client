package org.chiclepad.backend;

import org.chiclepad.backend.business.LocaleUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LocaleUtilsTest {

    @Test
    void getAllLocals() {
        List<Locale> locals = LocaleUtils.getAllLocals();

        assertThat(locals)
                .doesNotContainNull()
                .isNotEmpty();
    }

    @Test
    void getAllLocalsAsStrings() {
        assertThat(LocaleUtils.getAllLocalsAsStrings()).hasSameSizeAs(LocaleUtils.getAllLocals());
    }

    @Test
    void getReadableLocales() {
        assertThat(LocaleUtils.getReadableLocales()).hasSameSizeAs(LocaleUtils.getAllLocals());
    }

    @Test
    void getCodeFromReadableLocale() {
        List<String> readableLocales = LocaleUtils.getReadableLocales();
        List<Locale> allLocals = LocaleUtils.getAllLocals();

        List<String> codes1 = readableLocales.stream()
                .map(LocaleUtils::getCodeFromReadableLocale)
                .collect(Collectors.toList());

        List<String> codes2 = allLocals.stream()
                .map(LocaleUtils::codeFromLocale)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertThat(codes1).containsAll(codes2);
    }

    @Test
    void localeName() {
        List<String> expected = List.of("French (Belgium)", "Thai (Thailand)", "Polish (Poland)");
        List<String> got = List.of(
                LocaleUtils.localeName(Locale.forLanguageTag("fr-BE")),
                LocaleUtils.localeName(Locale.forLanguageTag("th-TH-u-ca-buddhist-nu-thai")),
                LocaleUtils.localeName("pl-PL")
        );

        assertThat(got).containsExactly(expected.toArray(new String[0]));
    }

    @Test
    void localeFromCode() {
        List<Locale> expected = List.of(
                Locale.forLanguageTag("fr-BE"),
                Locale.forLanguageTag("th-TH-u-ca-buddhist-nu-thai"),
                Locale.forLanguageTag("pl-PL")
        );
        List<Locale> got = List.of(
                LocaleUtils.localeFromCode("fr-BE"),
                LocaleUtils.localeFromCode("th-TH-u-ca-buddhist-nu-thai"),
                LocaleUtils.localeFromCode("pl-PL")
        );

        assertThat(got).containsExactly(expected.toArray(new Locale[0]));
    }

    @Test
    void codeFromLocale() {
        String code = "fr-BE";
        Locale locale = Locale.forLanguageTag(code);

        Optional<String> got = LocaleUtils.codeFromLocale(locale);
        assertThat(got)
                .isPresent()
                .isEqualTo(Optional.of(code));
    }

}
