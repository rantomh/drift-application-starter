package com.rantomah.drift.framework.web;

import com.rantomah.drift.framework.annotation.mapping.Delete;
import com.rantomah.drift.framework.annotation.mapping.Get;
import com.rantomah.drift.framework.annotation.mapping.Patch;
import com.rantomah.drift.framework.annotation.mapping.Post;
import com.rantomah.drift.framework.annotation.mapping.Put;
import com.rantomah.drift.framework.annotation.mapping.Request;
import com.rantomah.drift.framework.annotation.stereotype.MvcController;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MappingExtractor {

    public Set<Mapping> extractFrom(List<Object> controllers) {
        Set<Mapping> routes = new HashSet<>();
        for (Object controller : controllers) {
            Class<?> clazz = controller.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                Mapping mapping = findMappingAnnotation(method, controller);
                if (mapping == null) {
                    continue;
                }
                if (!routes.add(mapping)) {
                    throw new IllegalStateException(
                            String.format(
                                    "Mapping with path: %s and method: %s already added",
                                    mapping.path(), mapping.method()));
                }
            }
        }
        return routes;
    }

    private Mapping findMappingAnnotation(Method method, Object controller) {
        final boolean isMvc = controller.getClass().isAnnotationPresent(MvcController.class);
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation == null) {
                continue;
            }
            if (annotation instanceof Request maping) {
                return new Mapping(maping.path(), maping.method(), maping.consume(), maping.produce(), controller, method, isMvc);
            }
            if (annotation instanceof Get get) {
                return new Mapping(get.path(), "GET", get.consume(), get.produce(), controller, method, isMvc);
            }
            if (annotation instanceof Post post) {
                return new Mapping(post.path(), "POST", post.consume(), post.produce(), controller, method, isMvc);
            }
            if (annotation instanceof Put put) {
                return new Mapping(put.path(), "PUT", put.consume(), put.produce(), controller, method, isMvc);
            }
            if (annotation instanceof Patch patch) {
                return new Mapping(patch.path(), "PATCH", patch.consume(), patch.produce(), controller, method, isMvc);
            }
            if (annotation instanceof Delete delete) {
                return new Mapping(delete.path(), "DELETE", delete.consume(), delete.produce(), controller, method, isMvc);
            }
        }
        return null;
    }
}
