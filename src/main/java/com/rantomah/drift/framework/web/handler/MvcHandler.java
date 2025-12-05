package com.rantomah.drift.framework.web.handler;

import com.rantomah.drift.framework.core.Logger;
import com.rantomah.drift.framework.web.Mapping;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import java.lang.reflect.InvocationTargetException;

public class MvcHandler implements Handler<RoutingContext> {

    private final TemplateEngine templateEngine;
    private static final String TEMPLATE_PREFIX = "templates/";
    private static final String TEMPLATE_EXTENTION = ".html";

    public MvcHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(RoutingContext ctx) {
        Mapping mapping = ctx.get("mapping");
        String controllerClassName = mapping.controller().getClass().getName();
        String handlerName = mapping.handler().getName();
        try {
            Object response = mapping.handler().invoke(mapping.controller(), ctx);
            if (response instanceof String view) {
                templateEngine
                        .render(ctx.data(), TEMPLATE_PREFIX + view + TEMPLATE_EXTENTION)
                        .onSuccess(
                                buffer ->
                                        ctx.response()
                                                .putHeader("Content-Type", "text/html")
                                                .end(buffer))
                        .onFailure(ctx::fail);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Logger.exception.error("Failed to handle {}::{}", controllerClassName, handlerName, e);
            ctx.fail(e);
        }
    }
}
