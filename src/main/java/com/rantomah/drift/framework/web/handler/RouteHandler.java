package com.rantomah.drift.framework.web.handler;

import com.rantomah.drift.framework.core.Logger;
import com.rantomah.drift.framework.core.exception.DriftException;
import com.rantomah.drift.framework.web.Mapping;
import com.rantomah.drift.framework.web.Response;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.InvocationTargetException;

public class RouteHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext ctx) {
        Mapping mapping = ctx.get("mapping");
        try {
            Object result = mapping.handler().invoke(mapping.controller(), ctx);
            switch (result) {
                case Future<?> future ->
                    future.onComplete(ar -> {
                        if (ar.succeeded()) {
                            Object response = ar.result();
                            if (response instanceof Response<?> resp) {
                                resp.send(ctx);
                                return;
                            }
                        }
                        ctx.fail(ar.cause());
                    });
                case Response<?> resp ->
                    resp.send(ctx);
                default ->
                    ctx.fail(new DriftException("Unable to complete request"));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Logger.exception.error("Failed to handle {}::{}", mapping.controller().getClass().getName(), mapping.handler().getName(), e);
            ctx.fail(e);
        }
    }

}
