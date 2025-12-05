package com.rantomah.drift.framework.web.handler;

import com.rantomah.drift.framework.core.Logger;
import com.rantomah.drift.framework.core.exception.DriftException;
import com.rantomah.drift.framework.web.Mapping;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import java.lang.reflect.InvocationTargetException;

public class MvcHandler implements Handler<RoutingContext> {

    private final TemplateEngine templateEngine;
    private static final String TEMPLATE_PREFIX = "templates/";
    private static final String TEMPLATE_EXTENSION = ".html";

    public MvcHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(RoutingContext ctx) {
        Mapping mapping = ctx.get("mapping");
        String controllerClassName = mapping.controller().getClass().getName();
        String handlerName = mapping.handler().getName();

        try {
            Object result = mapping.handler().invoke(mapping.controller(), ctx);
            switch (result) {
                case Future<?> future ->
                    future.onComplete(ar -> {
                        if (ar.succeeded()) {
                            Object response = ar.result();
                            if (response instanceof String view) {
                                handleViewResult(ctx, view);
                                return;
                            }
                        }
                        ctx.fail(ar.cause());
                    });
                case String view ->
                    handleViewResult(ctx, view);
                default ->
                    ctx.fail(new DriftException("Unable to render MVC response"));
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            Logger.exception.error("Failed to handle {}::{}", controllerClassName, handlerName, e);
            ctx.fail(e);
        }
    }

    private void handleViewResult(RoutingContext ctx, Object result) {
        if (!(result instanceof String view)) {
            ctx.fail(new DriftException("MVC handler did not return a view name"));
            return;
        }

        templateEngine
                .render(ctx.data(), TEMPLATE_PREFIX + view + TEMPLATE_EXTENSION)
                .onSuccess(buffer
                        -> ctx.response()
                        .putHeader("Content-Type", "text/html")
                        .end(buffer))
                .onFailure(ctx::fail);
    }
}
