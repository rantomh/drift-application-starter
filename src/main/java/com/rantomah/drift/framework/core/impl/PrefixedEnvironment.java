package com.rantomah.drift.framework.core.impl;

import com.rantomah.drift.framework.core.Environment;
import java.util.Map;

public class PrefixedEnvironment implements Environment {

    private final Map<String, Object> properties;
    private final String profile;

    public PrefixedEnvironment(String profile, Map<String, Object> properties) {
        this.profile = profile;
        this.properties = properties;
    }

    @Override
    public String getProfile() {
        return profile.trim().toLowerCase();
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }

    private Object getObjectProperty(String key, Map<String, Object> map) {
        if (key == null || map == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        Object current = map;
        for (String part : parts) {
            if (!(current instanceof Map<?, ?> currentMap)) {
                return null;
            }
            if (!currentMap.containsKey(part)) {
                return null;
            }
            current = currentMap.get(part);
        }
        return current;
    }

    @Override
    public String getProperty(String key) {
        return (String) getObjectProperty(key, properties);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        Object property = getObjectProperty(key, properties);
        if (targetType.isInstance(property)) {
            return targetType.cast(property);
        }
        return null;
    }
}
