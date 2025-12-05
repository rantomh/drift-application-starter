package com.rantomah.drift.framework.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.Locale;

public class LocaleHandler implements Handler<RoutingContext> {

    private boolean isValidLang(String lang) {
        if (lang == null) return false; 
        return List.of("en", "en-us", "fr", "fr-FR").contains(lang.trim().toLowerCase());
    }

    @Override
    public void handle(RoutingContext ctx) {
        String header = ctx.request().getHeader("Accept-Language");
        Locale locale = Locale.ENGLISH;
        if (header != null && !header.isBlank()) {
            String lang = header.split(",")[0].split(";")[0];
            if (isValidLang(lang)) {
                locale = Locale.forLanguageTag(lang);
            }
        }
        ctx.put("locale", locale);
        ctx.put("t", "");
        ctx.next();
    }
}
