package com.rantomah.drift.framework.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTF8Control extends ResourceBundle.Control {

    @Override
    public ResourceBundle newBundle(
            String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {

        String bundleName = toBundleName(baseName, locale);
        String resourceName = bundleName.replace('.', '/') + ".properties";
        try (InputStream stream = loader.getResourceAsStream(resourceName)) {
            if (stream != null) {
                return new PropertyResourceBundle(
                        new InputStreamReader(stream, StandardCharsets.UTF_8));
            } else {
                return null;
            }
        }
    }
}
