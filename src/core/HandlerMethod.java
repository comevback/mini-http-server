package core;
import java.lang.reflect.Method;

public class HandlerMethod {
    public final Object controller;
    public final Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }
}
