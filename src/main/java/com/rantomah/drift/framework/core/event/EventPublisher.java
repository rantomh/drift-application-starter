package com.rantomah.drift.framework.core.event;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class EventPublisher {

    private final EventBus eventBus;
    private static final String TOPIC_NAME = "drift-event-topic";

    public EventPublisher(Vertx vertx) {
        this.eventBus = vertx.eventBus();
    }

    public <T> void publish(T event) {
        eventBus.publish(TOPIC_NAME, JsonObject.mapFrom(event));
    }
}
