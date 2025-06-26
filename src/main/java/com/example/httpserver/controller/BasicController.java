package com.example.httpserver.controller;
import com.example.httpserver.annotation.GetMapping;
import com.example.httpserver.annotation.PostMapping;
import com.example.httpserver.annotation.RequestBody;
import com.example.httpserver.annotation.RequestParam;
import com.example.httpserver.annotation.Controller;

/**
 * BasicController 是一个示例控制器，演示了如何使用注解来处理 HTTP 请求。
 * 它包含多个处理 GET 和 POST 请求的方法。
 */
@Controller
public class BasicController {
    @GetMapping("/hello")
    public String hello() {
        System.out.println("Got a GET request for /hello");
        return "Hello from annotated BasicController!";
    }

    @GetMapping("/bye")
    public String bye() {
        System.out.println("Got a GET request for /bye");
        return "Goodbye from annotated BasicController!";
    }

    @GetMapping("/greet")
    public String greet(@RequestParam("name") String name) {
        System.out.println("Got a GET request for /greet with name: " + name);
        return "Hello, " + name;
    }


    @PostMapping("/post")
    public String post() {
        System.out.println("Got a POST request for /post");
        return "Post request received in annotated BasicController!";
    }

    public static class User {
        public String name;
        public int age;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        System.out.println("Got a POST request for /user with user: " + user.name + ", age: " + user.age);
        User newUser = new User();
        newUser.name = user.name;
        newUser.age = user.age;
        return newUser;
    }
}
