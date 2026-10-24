package com.example.layers.level1.app;

/**
 * Starts the application: builds ALL the objects and runs a few commands.
 * Because it connects every layer, it is not part of any layer. It is the "composition root"
 * (you met it in the module dependency-injection). It lives OUTSIDE the layers, in the package of the level.
 */
public class ConsoleApp {

    public static void main(String[] args) {
        TaskController controller = new TaskController(new TaskService(new InMemoryTaskRepository()));
        for (String command : new String[]{"add anna Write docs", "add tom Fix bug", "done 1", "list"}) {
            System.out.println("> " + command);
            System.out.println(controller.handle(command));
        }
    }
}
