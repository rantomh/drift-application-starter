package com.rantomah.drift.framework.web.impl;

import com.rantomah.drift.framework.web.Mapping;
import com.rantomah.drift.framework.web.RouteRegister;
import com.rantomah.drift.framework.web.handler.MvcHandler;
import com.rantomah.drift.framework.web.handler.RouteHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import java.util.Set;

public class RouteRegisterImpl implements RouteRegister {

    private final RouteHandler routeHandler;
    private final MvcHandler mvcHandler;

    public RouteRegisterImpl(TemplateEngine templateEngine) {
        this.routeHandler = new RouteHandler();
        this.mvcHandler = new MvcHandler(templateEngine);
    }

    @Override
    public void register(Router router, Mapping mapping) {
        router.route(HttpMethod.valueOf(mapping.method()), mapping.path())
                .consumes(mapping.consume())
                .produces(mapping.produce())
                .handler(
                        ctx -> {
                            ctx.put("mapping", mapping);
                            if (mapping.isMvc()) {
                                mvcHandler.handle(ctx);
                            } else {
                                routeHandler.handle(ctx);
                            }
                        });
    }

    @Override
    public void register(Router router, Set<Mapping> mappings) {
        for (Mapping mapping : mappings) {
            register(router, mapping);
        }
    }
}
