package core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, HandlerMethod> handlerMethodHashMap = new HashMap<>();

    public Dispatcher() {}

    public void register(String httpMethod, String path, Class<?> clazz, Method method) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String key = httpMethod.toUpperCase() + ":" + path;
            handlerMethodHashMap.put(key, new HandlerMethod(instance, method));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public HandlerMethod dispatch(HttpRequest request) {
        String key = request.method.toUpperCase() + ":" + request.path;
        HandlerMethod handlerMethod = handlerMethodHashMap.get(key);
        if (handlerMethod == null) {
            throw new RuntimeException("404 Not Found: " + request.method + " " + request.path);
        }
        return handlerMethod;
    }
}