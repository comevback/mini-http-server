package core;

public class HandlerAdapter {
    public String handle(HandlerMethod handler) {
        try {
            return (String) handler.invoke();
        } catch (Exception e) {
            return "500 Internal Server Error: " + e.getMessage();
        }
    }
}
