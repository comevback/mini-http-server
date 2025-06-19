# mini-http-server

## 项目概述
`mini-http-server` 是一个使用 Java 编写的轻量级 HTTP 服务器，
通过自定义注解实现简单的路由映射。项目提供了基本的 GET/POST 接口，
并包含一个调用 Google 翻译服务的 `/translate` 示例。

## 目录结构
```
src/
  MainServer.java             # 启动入口
  annotation/                 # 路由及参数相关注解
  controller/                 # 控制器类
  core/                       # 核心调度与路由组件
  dto/                        # 请求和响应对象
  server/                     # HTTP 服务器实现
  service/                    # 业务逻辑，如翻译服务
  util/                       # 工具类
```

## 各主要类和包的作用
- **controller**：存放所有控制器，例如 `BasicController` 和 `TranslationController`，分别处理基础接口和翻译接口。
- **core**：包含 `Dispatcher`、`HandlerAdapter`、`RouterScanner` 等类，负责扫描路由、解析请求并调用相应方法。
- **service**：实现业务逻辑。`TranslateServiceImpl` 会向 Google Translate 发送 HTTP 请求获取翻译结果。
- **util**：如 `ClassScanner`、`ParamConverter` 等辅助工具，用于类扫描和参数转换。
- **server**：`HttpServer` 负责监听端口并接收客户端连接。
- **dto**：定义 `TranslateRequest` 与 `TranslateResponse` 等数据传输对象。

## 构建和运行步骤
在项目根目录执行：
```bash
mvn package
java -cp target/classes MainServer
```
服务器默认监听 `8080` 端口。

## 示例请求
- **获取问候**
  ```bash
  curl http://localhost:8080/hello
  ```
- **翻译文本**
  ```bash
  curl -X POST http://localhost:8080/translate \
       -H "Content-Type: application/json" \
       -d '{"text":"hello","sourceLanguage":"en","targetLanguage":"zh-CN"}'
  ```

## `/translate` 依赖网络
翻译接口会调用 Google Translate 的在线服务，因此必须在能访问外网的环境下运行。
如果无法连接互联网，调用 `/translate` 时会返回失败信息。