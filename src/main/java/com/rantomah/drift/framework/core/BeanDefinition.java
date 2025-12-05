package com.rantomah.drift.framework.core;

public interface BeanDefinition {

    String getId();

    String getType();

    Class<?> getClazz();

    Object getInstance();
}
