package core;

import java.io.*;
import java.net.Socket;

public class HandlerMapping {
    public static void handle(Socket client, Dispatcher dispatcher) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            String line = reader.readLine(); // 请求行：GET /hello HTTP/1.1
            if (line == null || !line.startsWith("GET") && !line.startsWith("POST")) return;

            String[] parts = line.split(" ");
            String httpMethod = parts[0];
            String path = parts[1];

            HandlerAdapter handlerAdapter = new HandlerAdapter();
            HandlerMethod handlerMethod = dispatcher.dispatch(httpMethod, path);
            String body = handlerAdapter.handle(handlerMethod);

            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: text/plain\r\n");
            writer.write("Content-Length: " + body.length() + "\r\n\r\n");
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
