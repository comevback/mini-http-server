package com.example.httpserver;

import com.example.httpserver.core.AnnotationScanner;
import com.example.httpserver.core.Dispatcher;
import com.example.httpserver.core.RouterScanner;
import com.example.httpserver.server.HttpServer;

public class MainServer {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();
        try {
            // 扫描所有包下的注解
            AnnotationScanner.scan("com.example.httpserver");
            RouterScanner.scan("com.example.httpserver.controller", dispatcher); // 扫描 controller 包下的所有类
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        HttpServer server = new HttpServer(8080, dispatcher); // 创建 HTTP 服务器
        server.start(); // 启动服务器
    }
}
