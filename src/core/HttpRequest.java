package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public String method; // 请求方法，如 GET、POST 等
    public String path; // 请求路径，如 /hello
    public String protocol; // 协议版本，如 HTTP/1.1
    public Map<String, String> headers = new HashMap<>(); // 存储请求头部信息
    public String body = ""; // 存储请求体内容，默认为空字符串
    public Map<String, String> queryParams = new HashMap<>(); // 存储查询参数

    public HttpRequest(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    /**
     * 解析 HTTP 请求
     * @param reader BufferedReader 用于读取请求内容
     * @return HttpRequest 对象，包含请求方法、路径、协议、头部和查询参数等信息
     * @throws IOException 如果读取请求时发生错误
     */
    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String line = reader.readLine(); // 请求行例：GET /hello?name=putuu HTTP/1.1
        if (line == null || line.isEmpty()) return null;

        String[] parts = line.split(" ");
        if (parts.length < 3) return null; // 避免数组越界异常
        String method = parts[0];
        String rawPath = parts[1];
        String protocol = parts[2];

        String path = rawPath.split("\\?")[0];

        HttpRequest request = new HttpRequest(method, path, protocol);

        // 解析 query 参数
        if (rawPath.contains("?")) {
            String query = rawPath.split("\\?")[1];
            for (String kv : query.split("&")) {
                String[] pair = kv.split("=");
                if (pair.length == 2) {
                    request.queryParams.put(pair[0], pair[1]);
                }
            }
        }

        // 解析 header
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            int idx = headerLine.indexOf(":");
            if (idx > 0) {
                String key = headerLine.substring(0, idx).trim();
                String value = headerLine.substring(idx + 1).trim();
                request.headers.put(key, value);
            }
        }

        // 如果是 POST 或有 body，再读 body
        if ("POST".equalsIgnoreCase(method)) {
            int len = Integer.parseInt(request.headers.getOrDefault("Content-Length", "0"));
            char[] bodyChars = new char[len];  // 创建一个字符数组来存储 body
            reader.read(bodyChars); // 读取指定长度的 body
            request.body = new String(bodyChars);  // 将字符数组转换为字符串，储存到 request.body 中
        }

        return request;
    }
}
