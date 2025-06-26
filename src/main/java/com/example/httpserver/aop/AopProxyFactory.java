package com.example.httpserver.aop;

import com.example.httpserver.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopProxyFactory {
    /**
     * 创建AOP代理对象
     * @param target 目标对象
     * @return AOP代理对象
     */
    public static Object createProxy(Object target) {
        Class<?> targetClass = target.getClass();
        Class<?>[] interfaces = targetClass.getInterfaces();

        return Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                interfaces,
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Method realMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());

                        if (realMethod.isAnnotationPresent(Log.class)) {
                            Log log = realMethod.getAnnotation(Log.class);
                            System.out.println("[AOP] Executing method: " +realMethod.getName() +" : "+ log.value());
                        }

                        return method.invoke(target, args);
                    }
                }
        );
    }
}
