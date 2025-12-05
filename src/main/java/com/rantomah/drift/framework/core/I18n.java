package com.rantomah.drift.framework.core;

import io.vertx.ext.web.RoutingContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public final class I18n {

    private I18n() {
    }

    private static final UTF8Control CONTROL = new UTF8Control();
    private static final Map<String, ResourceBundle> BUNDLE_CACHE = new ConcurrentHashMap<>();

    private static ResourceBundle getBundle(Locale locale) {
        Locale effectiveLocale = (locale != null ? locale : Locale.ENGLISH);
        String key = effectiveLocale.toLanguageTag();
        return BUNDLE_CACHE.computeIfAbsent(key,
                k -> ResourceBundle.getBundle("i18n.messages", effectiveLocale, CONTROL)
        );
    }

    public static String t(Locale locale, String key, Object... args) {
        try {
            ResourceBundle bundle = getBundle(locale);
            String pattern = bundle.getString(key);
            return (args.length == 0)
                    ? pattern
                    : MessageFormat.format(pattern, args);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }

    public static String t(RoutingContext ctx, String key, Object... args) {
        Locale locale = ctx != null ? (Locale) ctx.get("locale") : Locale.ENGLISH;
        return t(locale, key, args);
    }

}
