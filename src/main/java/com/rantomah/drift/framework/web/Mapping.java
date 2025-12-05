package com.rantomah.drift.framework.web;

import java.lang.reflect.Method;
import java.util.Objects;

public class Mapping {

    private final String path;
    private final String method;
    private final String consume;
    private final String produce;
    private final Object controller;
    private final Method handler;
    private final boolean mvc;

    public Mapping(
            String path,
            String method,
            String consume,
            String produce,
            Object controller,
            Method handler,
            boolean mvc) {
        this.path = path;
        this.method = method;
        this.consume = consume;
        this.produce = produce;
        this.controller = controller;
        this.handler = handler;
        this.mvc = mvc;
    }

    public String path() {
        return path;
    }

    public String method() {
        return method;
    }

    public String consume() {
        return consume;
    }

    public String produce() {
        return produce;
    }

    public Object controller() {
        return controller;
    }

    public Method handler() {
        return handler;
    }

    public boolean isMvc() {
        return mvc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mapping m)) {
            return false;
        }
        return Objects.equals(path, m.path)
                && Objects.equals(method, m.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
