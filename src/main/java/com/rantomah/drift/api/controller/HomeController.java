package com.rantomah.drift.api.controller;

import com.rantomah.drift.framework.annotation.di.Property;
import com.rantomah.drift.framework.annotation.mapping.Get;
import com.rantomah.drift.framework.annotation.stereotype.MvcController;
import com.rantomah.drift.framework.core.I18n;
import io.vertx.ext.web.RoutingContext;

@MvcController
public class HomeController {

    @Property("app.version")
    private String version;

    @Get
    public String home(RoutingContext ctx) {
        String message = I18n.t(ctx, "message.home", version);
        ctx.put("message", message);
        return "index";
    }
}
