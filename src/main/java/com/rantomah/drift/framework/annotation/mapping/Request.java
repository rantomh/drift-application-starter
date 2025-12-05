package com.rantomah.drift.framework.annotation.mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Request {

    public String path() default "";

    public String method() default "GET";

    public String consume() default "*/*";

    public String produce() default "application/json;charset=UTF-8";
}
