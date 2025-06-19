package core;

import java.lang.reflect.Method;
import java.util.Set;
import annotation.GetMapping;
import annotation.PostMapping;
import util.ClassScanner;


public class RouterScanner {
    public static void scan(String basePackage, Dispatcher dispatcher) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(basePackage); // 获取 controller 下的所有类

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String path = method.getAnnotation(GetMapping.class).value();
                    dispatcher.register("GET", path, clazz, method);
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    String path = method.getAnnotation(PostMapping.class).value();
                    dispatcher.register("POST", path, clazz, method);
                }
            }
        }
    }
}
