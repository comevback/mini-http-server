package controller;
import annotation.GetMapping;
import annotation.PostMapping;

public class BasicController {
    @GetMapping("/hello")
    public String hello() {
        System.out.println("Got a GET request for /hello");
        return "Hello from annotated HelloController!";
    }

    @GetMapping("/bye")
    public String bye() {
        System.out.println("Got a GET request for /bye");
        return "Goodbye from annotated HelloController!";
    }

    @PostMapping("/post")
    public String post() {
        System.out.println("Got a POST request for /post");
        return "Post request received in annotated HelloController!";
    }
}
