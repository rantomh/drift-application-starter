package com.rantomah.drift.framework.core;

import java.util.Collection;
import java.util.List;

public interface ApplicationContext {

    void registerBean(String id, String type, Object instance);

    void registerBean(String id, Object instance);

    <T> T getBean(String id, Class<T> clazz);

    <T> T getBean(Class<T> clazz);

    <T> List<T> getBeans(Class<T> clazz);

    <T> List<T> getBeans(String type);

    void autowireAll();

    void autowire(Object instance);

    Collection<BeanDefinition> getDefinitions();
}
