package com.rantomah.drift.framework.core;

import com.rantomah.drift.framework.web.WebVerticle;
import io.vertx.launcher.application.VertxApplication;

public class DriftRunner {

    private DriftRunner() {}

    public static int run(Class<?> clazz) {
        VertxApplication vertxApplication = new VertxApplication(new String[] {WebVerticle.class.getName()});
        return vertxApplication.launch();
    }
}
