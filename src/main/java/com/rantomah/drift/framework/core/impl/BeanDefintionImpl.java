package com.rantomah.drift.framework.core.impl;

import com.rantomah.drift.framework.core.BeanDefinition;

public class BeanDefintionImpl implements BeanDefinition {

    private final String id;
    private final String type;
    private final Class<?> clazz;
    private final Object instance;

    public BeanDefintionImpl(String id, String type, Class<?> clazz, Object instance) {
        this.id = id;
        this.type = type;
        this.clazz = clazz;
        this.instance = instance;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
}
