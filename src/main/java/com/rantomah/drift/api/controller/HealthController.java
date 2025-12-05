package com.rantomah.drift.api.controller;

import com.rantomah.drift.api.dto.HealthDto;
import com.rantomah.drift.api.service.HealthService;
import com.rantomah.drift.framework.annotation.di.Inject;
import com.rantomah.drift.framework.annotation.mapping.Get;
import com.rantomah.drift.framework.annotation.stereotype.Controller;
import com.rantomah.drift.framework.web.AbstractController;
import com.rantomah.drift.framework.web.Response;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

@Controller
public class HealthController extends AbstractController {

    @Inject
    private HealthService healthService;

    @Get(path = "/api/health")
    public Future<Response<HealthDto>> health(RoutingContext ctx) {
        return healthService.getHealth().map(this::success);
    }
}
