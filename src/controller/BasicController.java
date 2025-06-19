package controller;
import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestBody;
import annotation.RequestParam;

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
    public String createUser(@RequestBody User user) {
        System.out.println("Got a POST request for /user with user: " + user.name + ", age: " + user.age);
        return "Hello, " + user.name + "! You are " + user.age;
    }
}
