package com.rantomah.drift.framework.core.event;

import com.rantomah.drift.framework.annotation.EventListener;
import com.rantomah.drift.framework.core.Logger;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventListenerRegistry {

    private final Vertx vertx;
    private static final String TOPIC_NAME = "drift-event-topic";

    public EventListenerRegistry(Vertx vertx) {
        this.vertx = vertx;
    }

    public void registerListeners(Object... beans) {
        for (Object bean : beans) {
            registerBean(bean);
        }
    }

    private void registerBean(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            EventListener ann = method.getAnnotation(EventListener.class);
            if (ann == null) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) {
                throw new IllegalArgumentException("Method <" + method + "> have more than one parameter");
            }
            Class<?> eventType = params[0];
            if (!method.canAccess(bean)) {
                method.setAccessible(true);
            }
            vertx.eventBus()
                    .consumer(TOPIC_NAME, msg -> handleEvent(msg, eventType, method, bean));
        }
    }

    private void handleEvent(Message<Object> msg, Class<?> eventType, Method method, Object bean) {
        try {
            Object event;
            Object body = msg.body();
            if (body instanceof JsonObject jsonObject) {
                event = jsonObject.mapTo(eventType);
            } else {
                if (eventType.isInstance(body)) {
                    event = body;
                } else {
                    event = JsonObject.mapFrom(body).mapTo(eventType);
                }
            }
            method.invoke(bean, event);
        } catch (IllegalAccessException | InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            Logger.exception.error("Error on consuming event", cause);
        }
    }
}
