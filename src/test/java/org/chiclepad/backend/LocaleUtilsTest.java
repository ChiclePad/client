package org.chiclepad.backend;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

class LocaleUtilsTest {

    @Test
    void getAllLocals() {
        List<Locale> locals = LocaleUtils.getAllLocals();

        assertThat(locals)
                .doesNotContainNull()
                .isNotEmpty();
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