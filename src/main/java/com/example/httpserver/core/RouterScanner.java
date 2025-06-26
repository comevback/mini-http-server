package com.example.httpserver.core;

import java.lang.reflect.Method;
import java.util.Set;
import com.example.httpserver.annotation.GetMapping;
import com.example.httpserver.annotation.PostMapping;
import com.example.httpserver.util.ClassScanner;


public class RouterScanner {
    public static void scan(String basePackage, Dispatcher dispatcher) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(basePackage); // 获取 controller 下的所有类

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                Object controllerInstance = AnnotationScanner.getBean(clazz);
                if (controllerInstance == null) {
                    throw new RuntimeException("Failed to get bean for class: " + clazz.getName());
                }
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String path = method.getAnnotation(GetMapping.class).value();
                    dispatcher.register("GET", path, controllerInstance, method);
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    String path = method.getAnnotation(PostMapping.class).value();
                    dispatcher.register("POST", path, controllerInstance, method);
                }
            }
        }
    }
}
