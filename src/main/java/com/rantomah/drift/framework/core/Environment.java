package com.rantomah.drift.framework.core;

public interface Environment {

    String getProfile();

    boolean containsProperty(String key);

    String getProperty(String key);

    <T extends Object> T getProperty(String key, Class<T> targetType);
}
