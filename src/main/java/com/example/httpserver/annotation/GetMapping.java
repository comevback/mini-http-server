package com.example.httpserver.annotation;

import java.lang.annotation.*;

// 自定义注解，用于标记处理 GET 请求的方法
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    String value();
}
