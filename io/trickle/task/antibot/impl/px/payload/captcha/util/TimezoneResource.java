/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.util.Locale;

public class TimezoneResource {
    public static String[] SEEN_LANGUAGES = new String[]{"en-US", "zh-CN", "de", "de-de", "en-CA", "en-us", "en-GB", "el", "da-dk", "lt-LT", "de-DE", "cs", "en-gb", "ru-RU", "sv-se", "hu-HU", "ko-KR", "it-IT", "nl-NL", "en-AU", "fr-FR", "tr-TR", "es", "uk-UA", "fi-FI", "pt-PT", "en-IN", "zh-TW", "zh-ES", "pl-PL", "nl-nl", "pt-BR", "ja", "es-419", "es-ES", "vi-VN", "he-IL", "it-it", "fr-fr", "ro-RO", "en-ca", "nb-NO", "es-US", "en-sg", "cs-CZ", "en-IE", "sv-SE", "de-AT", "fr-BE", "fr-ca", "fr-be", "en-in", "fr-CA", "en-au"};

    public static Locale getLocalFromLanguage(String string) {
        String[] stringArray = string.split("-");
        if (stringArray.length != 2) return new Locale(stringArray[0]);
        return new Locale(stringArray[0], stringArray[1]);
    }
}
