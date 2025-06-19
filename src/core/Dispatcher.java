package core;

import annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, HandlerMethod> routeMap = new HashMap<>();

    public Dispatcher() {}

    public void register(String httpMethod, String path, Class<?> clazz, Method method) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String key = httpMethod.toUpperCase() + ":" + path;
            routeMap.put(key, new HandlerMethod(instance, method));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public HandlerMethod dispatch(String httpMethod, String path) {
        String key = httpMethod.toUpperCase() + ":" + path;
        HandlerMethod handlerMethod = routeMap.get(key);
        if (handlerMethod == null) {
            throw new RuntimeException("404 Not Found: " + httpMethod + " " + path);
        }
        return handlerMethod;
    }
}