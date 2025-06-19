package server;

import core.Dispatcher;
import core.HandlerMapping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

            while (true) {
                Socket client = serverSocket.accept();
                // 分发给请求处理器（单线程/线程池都可以）
                new Thread(() -> HandlerMapping.handle(client, dispatcher)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to start server: " + e.getMessage(), e);
        }
    }
}
