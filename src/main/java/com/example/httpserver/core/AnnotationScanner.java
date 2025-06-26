package com.example.httpserver.core;

import com.example.httpserver.annotation.Controller;
import com.example.httpserver.annotation.MyAutoWired;
import com.example.httpserver.annotation.Service;
import com.example.httpserver.aop.AopProxyFactory;
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

    /**
     * 获取指定类的单例实例
     * @param clazz 要获取实例的类
     * @return 单例实例，如果不存在则返回 null
     */
    public static Object getBean(Class<?> clazz) {
        return singletonCache.get(clazz);
    }

    /**
     * 获取或创建指定类的单例实例，并注入依赖
     * @param clazz 要创建实例的类
     * @return 创建的实例
     * @throws Exception 如果创建实例或注入依赖失败
     */
    private static Object getOrCreateInstance(Class<?> clazz) throws Exception {
        if (singletonCache.containsKey(clazz)) {
            return singletonCache.get(clazz);
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 如果类实现了接口，则创建代理实例
        if (clazz.getInterfaces().length > 0) {
            instance = AopProxyFactory.createProxy(instance);
        }
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
                    dependency = getOrCreateInstance(implClass);
                } else {
                    dependency = getOrCreateInstance(fieldType);
                }

                field.set(instance, dependency);
            }
        }
        return instance;
    }

    /**
     * 扫描指定包下的所有类，并创建单例对象
     * @param packageName 要扫描的包名
     * @throws Exception 如果创建实例或注入依赖失败
     */
    public static void scan(String packageName) throws Exception {
        System.out.println("Scanning package: " + packageName);
        // 使用一个哈希表来存储单例对象
        Set<Class<?>> classes = ClassScanner.getClasses(packageName); // 获取指定包下的所有类
        // System.out.println("Found " + classes.size() + " classes");
        interfaceToImplMap = InterfaceMapping.getInterfaceToImplMap(classes); // 获取接口与实现类的映射

        for (Class<?> clazz : classes) {
            if (!clazz.isInterface() && clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                // System.out.println("Creating instance for class: " + clazz.getName());
                getOrCreateInstance(clazz);
            }
        }
        System.out.println("Finished scanning and creating instances");
    }
}
