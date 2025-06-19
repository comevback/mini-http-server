package core;

import annotation.RequestBody;
import annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerAdapter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String handle(HandlerMethod handlerMethod, HttpRequest request) {
        try {
            Method method = handlerMethod.method;
            Object controller = handlerMethod.controller;

            Parameter[] parameters = method.getParameters(); // 获取方法参数列表
            Object[] args = new Object[parameters.length]; // 创建参数数组

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
                    args[i] = value; // 这里只处理 String，可扩展类型转换
                // 如果是一个 RequestBody 注解，反序列化 JSON 请求体到对应的类型
                } else if (param.isAnnotationPresent(RequestBody.class)) {
                    args[i] = objectMapper.readValue(request.body, type); // 反序列化 JSON
                } else {
                    args[i] = null;
                }
            }

            return (String) method.invoke(controller, args);
        } catch (Exception e) {
            e.printStackTrace();
            return "500 Internal Server Error: " + e.getMessage();
        }
    }
}
