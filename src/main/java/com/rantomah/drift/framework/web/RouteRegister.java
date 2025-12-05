package com.rantomah.drift.framework.web;

import io.vertx.ext.web.Router;
import java.util.Set;

public interface RouteRegister {
    void register(Router router, Set<Mapping> mappings);

    void register(Router router, Mapping mapping);
}
