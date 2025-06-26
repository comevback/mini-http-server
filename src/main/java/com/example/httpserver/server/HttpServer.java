package com.example.httpserver.server;

import com.example.httpserver.core.Dispatcher;
import com.example.httpserver.core.HandlerMapping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServer {
    private final int port;
    private final Dispatcher dispatcher;

    public HttpServer(int port, Dispatcher dispatcher) {
        this.port = port;
        this.dispatcher = dispatcher;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("🚀 HTTP Server started on port " + port);

            ThreadPoolExecutor pool = new ThreadPoolExecutor(
                    10, // core pool size
                    50, // max pool size
                    60L, // keep-alive time
                    java.util.concurrent.TimeUnit.SECONDS,
                    new java.util.concurrent.LinkedBlockingQueue<>(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

            while (true) {
                Socket client = serverSocket.accept();
                // new Thread(() -> HandlerMapping.handle(client, dispatcher)).start(); // 创建单个线程处理请求
                pool.execute(() -> HandlerMapping.handle(client, dispatcher)); // 使用线程池处理请求
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to start server: " + e.getMessage(), e);
        }
    }
}
