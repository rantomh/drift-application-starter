package com.rantomah.drift.framework.web;

public abstract class AbstractController {

    public <T> Response<T> success(T body) {
        return Response.<T>builder().status(200).body(body).build();
    }
}
