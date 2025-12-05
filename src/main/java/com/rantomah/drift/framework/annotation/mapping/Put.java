package com.rantomah.drift.framework.annotation.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Request
public @interface Put {

    public final String METHOD = "PUT";

    public String path() default "";

    public String consume() default "*/*";

    public String produce() default "application/json;charset=UTF-8";
}
