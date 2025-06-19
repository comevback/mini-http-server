import core.Dispatcher;
import core.RouterScanner;
import server.HttpServer;

public class MainServer {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();
        try {
            RouterScanner.scan("controller", dispatcher); // 扫描 controller 包下的所有类
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        HttpServer server = new HttpServer(8080, dispatcher); // 创建 HTTP 服务器
        server.start(); // 启动服务器
    }
}
