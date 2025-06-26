package com.example.httpserver.core;

import com.example.httpserver.annotation.Controller;
import com.example.httpserver.annotation.MyAutoWired;
import com.example.httpserver.annotation.Service;
import com.example.httpserver.util.ClassScanner;
import com.example.httpserver.util.InterfaceMapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationScanner {
    public static final Map<Class<?>, Object> singletonCache = new HashMap<>();
    public static Map<Class<?>, List<Class<?>>> interfaceToImplMap;

    public static Object getBean(Class<?> clazz) {
        return singletonCache.get(clazz);
    }

    private static Object getOrCreateInstance(Class<?> clazz) throws Exception {
        if (singletonCache.containsKey(clazz)) {
            return singletonCache.get(clazz);
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        singletonCache.put(clazz, instance); // 先缓存，防止循环依赖栈溢出

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyAutoWired.class)) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object dependency;

                if (fieldType.isInterface()) {
                    // 通过接口找实现类
                    List<Class<?>> implClasses = interfaceToImplMap.get(fieldType);
                    if (implClasses == null || implClasses.isEmpty()) {
                        throw new RuntimeException("No implementation found for " + fieldType.getName());
                    }
                    Class<?> implClass = implClasses.get(0);
                    dependency = getOrCreateInstance(implClass); // 🔁 递归注入
                } else {
                    dependency = getOrCreateInstance(fieldType); // 🔁 递归注入
                }

                field.set(instance, dependency);
            }
        }
        return instance;
    }

    public static void scan(String packageName) throws Exception {
        System.out.println("Scanning package: " + packageName);
        // 使用一个哈希表来存储单例对象
        Set<Class<?>> classes = ClassScanner.getClasses(packageName); // 获取指定包下的所有类
        System.out.println("Found " + classes.size() + " classes");
        interfaceToImplMap = InterfaceMapping.getInterfaceToImplMap(classes); // 获取接口与实现类的映射

        for (Class<?> clazz : classes) {
            if (!clazz.isInterface() && clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                System.out.println("Creating instance for class: " + clazz.getName());
                getOrCreateInstance(clazz);
            }
        }
        System.out.println("Finished scanning and creating instances");
    }
}
