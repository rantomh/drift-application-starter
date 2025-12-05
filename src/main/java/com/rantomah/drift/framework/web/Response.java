package com.rantomah.drift.framework.web;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;

public class Response<T> {

    private int status;
    private Map<String, String> headers = new HashMap<>();
    private T body;

    public int status() {
        return status;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public T body() {
        return body;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private int status = 200;
        private final Map<String, String> headers = new HashMap<>();
        private T body;

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> header(String header, String value) {
            this.headers.put(header, value);
            return this;
        }

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public Response<T> build() {
            Response<T> res = new Response<>();
            res.status = status;
            res.headers = headers;
            res.body = body;
            return res;
        }
    }

    public void send(RoutingContext rc) {
        if (rc == null) {
            throw new IllegalStateException("RoutingContext is required to send HTTP response");
        }
        headers.forEach((key, value) -> rc.response().putHeader(key.trim(), value.trim()));
        rc.response()
                .putHeader("Content-Type", "application/json;charset=UTF-8")
                .setStatusCode(status)
                .end(body == null ? "" : Json.encodePrettily(body));
    }
}
