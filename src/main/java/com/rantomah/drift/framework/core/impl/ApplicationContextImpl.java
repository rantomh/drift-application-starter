package com.rantomah.drift.framework.core.impl;

import com.rantomah.drift.framework.annotation.di.Inject;
import com.rantomah.drift.framework.annotation.di.Property;
import com.rantomah.drift.framework.core.ApplicationContext;
import com.rantomah.drift.framework.core.BeanDefinition;
import com.rantomah.drift.framework.core.Environment;
import com.rantomah.drift.framework.core.exception.DriftException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApplicationContextImpl implements ApplicationContext {

    private final Map<String, BeanDefinition> definitions = new ConcurrentHashMap<>();

    @Override
    public void registerBean(String id, String type, Object instance) {
        BeanDefinition def = new BeanDefintionImpl(id, type, instance.getClass(), instance);
        definitions.put(id, def);
    }

    @Override
    public void registerBean(String id, Object instance) {
        BeanDefinition def =
                new BeanDefintionImpl(
                        id, instance.getClass().getName(), instance.getClass(), instance);
        definitions.put(id, def);
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        BeanDefinition def = definitions.get(id);
        if (def == null) {
            throw new DriftException("No bean with id <" + id + ">");
        }
        return clazz.cast(def.getInstance());
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        List<T> beans = getBeans(clazz);
        if (beans.isEmpty()) {
            throw new DriftException("No bean of type <" + clazz.getName() + ">");
        }
        if (beans.size() > 1) {
            throw new DriftException("Multiple beans of type <" + clazz.getName() + "> found");
        }
        return beans.get(0);
    }

    @Override
    public <T> List<T> getBeans(Class<T> clazz) {
        return definitions.values().stream()
                .filter(def -> clazz.isAssignableFrom(def.getClazz()))
                .map(def -> clazz.cast(def.getInstance()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> getBeans(String type) {
        return definitions.values().stream()
                .filter(def -> type.equals(def.getType()))
                .map(def -> def.getInstance())
                .collect(Collectors.toList());
    }

    @Override
    public void autowireAll() {
        for (BeanDefinition def : definitions.values()) {
            Object instance = def.getInstance();
            autowire(instance);
        }
    }

    private boolean isConverteable(Field field) {
        return field.getType().isAssignableFrom(String.class)
                || field.getType().isAssignableFrom(Integer.class)
                || field.getType().isAssignableFrom(Float.class)
                || field.getType().isAssignableFrom(Double.class)
                || field.getType().isAssignableFrom(Boolean.class);
    }

    @Override
    public void autowire(Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            final String fieldName = field.getName();
            if (field.isAnnotationPresent(Inject.class)
                    && field.isAnnotationPresent(Property.class)) {
                throw new IllegalStateException("Field <" + fieldName + "> is injected and converted at same time");
            }
            if (field.isAnnotationPresent(Inject.class)) {
                inject(field, instance);
            }
            if (field.isAnnotationPresent(Property.class)) {
                populateField(field, instance);
            }
        }
    }

    private void inject(Field field, Object instance) {
        if (isConverteable(field)) {
            throw new IllegalStateException("Convertible field <" + field.getName() + "> cannot injected");
        }
        Object dependency = getBean(field.getType());
        field.setAccessible(true);
        try {
            field.set(instance, dependency);
        } catch (IllegalAccessException e) {
            throw new DriftException("Failed to inject field <" + field + ">", e);
        }
    }

    private void populateField(Field field, Object instance) {
        String fieldName = field.getName();
        if (!isConverteable(field)) {
            throw new IllegalStateException("Field <" + fieldName + "> is not converteable");
        }
        Property property = field.getAnnotation(Property.class);
        String key = property.value();
        Environment environment = getBean(Environment.class);
        Object value = environment.getProperty(key, field.getType());
        if (value == null) {
            throw new IllegalStateException("No value is converteable for Field <" + fieldName + ">");
        }
        field.setAccessible(true);
        try {
            field.set(instance, field.getType().cast(value));
        } catch (IllegalAccessException e) {
            throw new DriftException("Failed to inject field <" + fieldName + ">", e);
        }
    }

    @Override
    public Collection<BeanDefinition> getDefinitions() {
        return definitions.values();
    }
}
