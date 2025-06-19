package core;

import java.io.*;
import java.net.Socket;

public class HandlerMapping {
    public static void handle(Socket client, Dispatcher dispatcher) {
        try (
                // Socket autoCloseSocket = client; // 这行代码在 Java 9 及以上版本中可以使用，自动关闭 Socket
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            HttpRequest request = HttpRequest.parse(reader);
            if (request == null) return;

            HandlerMethod handlerMethod = dispatcher.dispatch(request);
            if (handlerMethod == null) {
                writer.write("HTTP/1.1 404 Not Found\r\n\r\n");
                writer.flush();
                return;
            }

            HandlerAdapter adapter = new HandlerAdapter();
            String responseBody = adapter.handle(handlerMethod, request);

//            writer.write("HTTP/1.1 200 OK\r\n");
//            writer.write("Content-Type: text/plain\r\n");
//            writer.write("Content-Length: " + responseBody.length() + "\r\n\r\n");
//            writer.write(responseBody);
//            writer.flush();
            byte[] bytes = responseBody.getBytes("UTF-8");

            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: application/json\r\n");
            writer.write("Content-Length: " + bytes.length + "\r\n");
            writer.write("\r\n");
            writer.flush(); // flush headers first
            client.getOutputStream().write(bytes); // 注意直接写字节流
            client.getOutputStream().flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
