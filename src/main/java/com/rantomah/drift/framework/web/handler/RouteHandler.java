package com.rantomah.drift.framework.web.handler;

import com.rantomah.drift.framework.core.Logger;
import com.rantomah.drift.framework.web.Mapping;
import com.rantomah.drift.framework.web.Response;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.InvocationTargetException;

public class RouteHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext ctx) {
        Mapping mapping = ctx.get("mapping");
        String controllerClassName = mapping.controller().getClass().getName();
        String handlerName = mapping.handler().getName();
        try {
            Object response = mapping.handler().invoke(mapping.controller(), ctx);
            if (response instanceof Response<?> resp) {
                resp.send(ctx);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Logger.exception.error("Failed to handle {}::{}", controllerClassName, handlerName, e);
            ctx.fail(e);
        }
    }
}
