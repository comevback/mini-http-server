package com.example.httpserver.core;

import com.example.httpserver.annotation.RequestBody;
import com.example.httpserver.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.httpserver.util.ParamConverter;

public class HandlerAdapter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String handle(HandlerMethod handlerMethod, HttpRequest request) {
        try {
            Method method = handlerMethod.method;
            Object controller = handlerMethod.controller;

            Parameter[] parameters = method.getParameters(); // 获取方法参数列表
            Object[] args = new Object[parameters.length]; // 创建参数数组

            /* 遍历参数列表，根据参数类型和注解进行处理
             * 1. 如果是 HttpRequest 类型，直接注入 request 对象
             * 2. 如果是 RequestParam 注解，获取查询参数并转换类型
             * 3. 如果是 RequestBody 注解，反序列化 JSON 请求体到对应的类型
             * 例如：
             *   public String myMethod(HttpRequest request, @RequestParam("id") int id, @RequestBody MyObject obj) { ... }
             */
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> type = param.getType();

                // 支持 HttpRequest 注入
                if (type == HttpRequest.class) {
                    args[i] = request;
                // 如果是一个 RequestParam 注解，根据注解的 value 获取查询参数，赋值给对应的arg[i]
                } else if (param.isAnnotationPresent(RequestParam.class)) {
                    String key = param.getAnnotation(RequestParam.class).value();
                    String value = request.queryParams.get(key);
                    args[i] = ParamConverter.convert(value, type); // 转换参数类型
                // 如果是一个 RequestBody 注解，反序列化 JSON 请求体到对应的类型
                } else if (param.isAnnotationPresent(RequestBody.class)) {
                    args[i] = objectMapper.readValue(request.body, type); // 反序列化 JSON
                } else {
                    args[i] = null;
                }
            }

            Object result = method.invoke(controller, args); // 调用方法
            if (result instanceof String) {
                return (String) result; // 纯字符串直接返回
            } else {
                return objectMapper.writeValueAsString(result); // 自动转 JSON
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "500 Internal Server Error: " + e.getMessage();
        }
    }
}
