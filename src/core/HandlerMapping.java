package core;

import java.io.*;
import java.net.Socket;

public class HandlerMapping {
    public static void handle(Socket client, Dispatcher dispatcher) {
        try (
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

            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: text/plain\r\n");
            writer.write("Content-Length: " + responseBody.length() + "\r\n\r\n");
            writer.write(responseBody);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
