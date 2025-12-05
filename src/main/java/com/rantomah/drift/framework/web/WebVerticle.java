package com.rantomah.drift.framework.web;

import com.rantomah.drift.api.controller.HealthController;
import com.rantomah.drift.api.controller.HomeController;
import com.rantomah.drift.api.event.TestEventHandler;
import com.rantomah.drift.api.service.HealthServiceImpl;
import com.rantomah.drift.framework.annotation.DriftApplication;
import com.rantomah.drift.framework.annotation.stereotype.Controller;
import com.rantomah.drift.framework.annotation.stereotype.MvcController;
import com.rantomah.drift.framework.core.ApplicationContext;
import com.rantomah.drift.framework.core.ApplicationContextFactory;
import com.rantomah.drift.framework.core.ConfigLoader;
import com.rantomah.drift.framework.core.Environment;
import com.rantomah.drift.framework.core.Logger;
import com.rantomah.drift.framework.core.event.EventListenerRegistry;
import com.rantomah.drift.framework.core.event.EventPublisher;
import com.rantomah.drift.framework.web.handler.LocaleHandler;
import com.rantomah.drift.framework.web.impl.RouteRegisterImpl;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import java.util.List;
import java.util.Set;

@DriftApplication
public class WebVerticle extends VerticleBase {

    private ApplicationContext applicationContext;
    private Environment environment;

    private void registerCoreBeans() {
        applicationContext.registerBean("vertx", Vertx.class.getName(), vertx);
        applicationContext.registerBean("environment", Environment.class.getName(), environment);
        applicationContext.registerBean("eventPublisher", EventPublisher.class.getName(), new EventPublisher(vertx));
    }

    private void registerAppBeans() {
        applicationContext.registerBean("healthServiceImpl", HealthServiceImpl.class.getName(), new HealthServiceImpl());
        applicationContext.registerBean("healthController", Controller.class.getName(), new HealthController());
        applicationContext.registerBean("homeController", MvcController.class.getName(), new HomeController());
    }

    private void registerListeners() {
        EventListenerRegistry eventListenerRegistry = new EventListenerRegistry(vertx);
        eventListenerRegistry.registerListeners(new TestEventHandler());
    }

    private Router setupRouter() {
        Router router = Router.router(vertx);
        router.route().handler(new LocaleHandler());
        router.route()
                .handler(
                        StaticHandler.create("public/")
                                .setCachingEnabled(true)
                                .setDirectoryListing(false)
                                .setEnableFSTuning(true)
                                .setFilesReadOnly(true));
        return router;
    }

    private void registerRoutes(Router router) {
        List<Object> controllers = applicationContext.getBeans(Controller.class.getName());
        List<Object> mvcControllers = applicationContext.getBeans(MvcController.class.getName());
        controllers.addAll(mvcControllers);
        Set<Mapping> mappings = new MappingExtractor().extractFrom(controllers);
        RouteRegister routeRegiter = new RouteRegisterImpl(ThymeleafTemplateEngine.create(vertx));
        routeRegiter.register(router, mappings);
    }

    private Future<HttpServer> startHttpServer(Router router) {
        return vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true))
                .requestHandler(router)
                .listen(environment.getProperty("server.port", Integer.class));
    }

    @Override
    public Future<HttpServer> start() {
        return vertx.<Router>executeBlocking(() -> {
            environment = ConfigLoader.load();
            applicationContext = ApplicationContextFactory.create();
            registerCoreBeans();
            registerAppBeans();
            applicationContext.autowireAll();
            registerListeners();
            Router router = setupRouter();
            registerRoutes(router);
            return router;
        }).compose(this::startHttpServer)
                .onSuccess(server -> Logger.log.info("Server started on port: {}", server.actualPort()))
                .onFailure(ex -> {
                    Logger.exception.error("Error on starting web Server", ex);
                    System.exit(-1);
                });
    }
}
