package com.example.httpserver.core;

import java.lang.reflect.Method;
import java.util.Set;
import com.example.httpserver.annotation.GetMapping;
import com.example.httpserver.annotation.PostMapping;
import com.example.httpserver.util.ClassScanner;


public class RouterScanner {
    /**
     * 扫描指定包下的所有 Controller 类，并注册其方法到 Dispatcher 中
     *
     * @param basePackage 要扫描的基础包名
     * @param dispatcher  Dispatcher 实例，用于注册路由
     * @throws Exception 如果扫描或注册过程中发生错误
     */
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
