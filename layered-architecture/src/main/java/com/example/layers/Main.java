package com.example.layers;

import com.example.layers.level2.presentation.TaskController;
import com.example.layers.level2.repository.InMemoryTaskRepository;
import com.example.layers.level2.service.TaskService;
import com.example.layers.level6.repository.AuditingTaskRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the six levels working. Run it to SEE your code.
 * (The tests are the real check. This is the "look at it" version.)
 *
 * PROVIDED CODE - do not change.
 */
public class Main {

    public static void main(String[] args) {
        section("Level 1 - the classes are in their layers", () -> {
            try {
                Class.forName("com.example.layers.level1.ConsoleApp")
                        .getMethod("main", String[].class).invoke(null, (Object) new String[0]);
            } catch (ClassNotFoundException e) {
                System.out.println("  ConsoleApp is not in com.example.layers.level1 yet (move the classes: Task 1)");
            } catch (ReflectiveOperationException e) {
                System.out.println("  ConsoleApp failed: " + e);
            }
        });

        section("Level 2 - the same behavior, with the layers in order", () -> {
            TaskController controller = new TaskController(new TaskService(new InMemoryTaskRepository()));
            controller.handle("add anna Write docs");
            System.out.println("  " + controller.handle("list") + "   (must be: 1 [ ] Write docs (anna))");
            System.out.println("  (the ArchUnit test Level2 tells you if the layers are in order: mvn test -Dtest=LayerRulesTest)");
        });

        section("Level 3 - the controller gives DTOs", () -> {
            com.example.layers.level3.service.TaskService service =
                    new com.example.layers.level3.service.TaskService(new com.example.layers.level3.repository.InMemoryTaskRepository());
            service.create("anna", "Write docs");
            Object result = new com.example.layers.level3.presentation.TaskController(service).get(1);
            System.out.println("  get(1) returns a: " + result.getClass().getSimpleName() + "   (must be: TaskResponse)");
        });

        section("Level 4 - errors are answers, not exceptions", () -> {
            com.example.layers.level4.presentation.TaskController controller = new com.example.layers.level4.presentation.TaskController(
                    new com.example.layers.level4.service.TaskService(new com.example.layers.level4.repository.InMemoryTaskRepository()));
            controller.handle("add anna Write docs");
            System.out.println("  done 99     -> " + controller.handle("done 99") + "   (must be: Error 404: task 99 not found)");
            System.out.println("  done 1      -> " + controller.handle("done 1"));
            System.out.println("  done 1      -> " + controller.handle("done 1") + "   (must be: Error 409: task 1 is already done)");
            System.out.println("  done abc    -> " + controller.handle("done abc") + "   (must be: Error 400: id must be a number)");
        });

        section("Level 5 - a new feature through all the layers", () -> {
            com.example.layers.level5.presentation.TaskController controller = new com.example.layers.level5.presentation.TaskController(
                    new com.example.layers.level5.service.TaskService(new com.example.layers.level5.repository.InMemoryTaskRepository()));
            controller.handle("add anna Write docs");
            System.out.println("  reassign 1 tom -> " + controller.handle("reassign 1 tom") + "   (must be: Task 1 reassigned to tom)");
        });

        section("Level 6 - wrap the data layer, change nothing else", () -> {
            List<String> log = new ArrayList<>();
            com.example.layers.level6.presentation.TaskController controller = new com.example.layers.level6.presentation.TaskController(
                    new com.example.layers.level6.service.TaskService(
                            new AuditingTaskRepository(new com.example.layers.level6.repository.InMemoryTaskRepository(), log)));
            controller.handle("add anna Write docs");
            controller.handle("done 1");
            System.out.println("  audit log: " + log + "   (must be: [findAll, save 1, findById 1, save 1])");
        });
    }

    // Runs one part. If your code is not finished, it prints the problem and goes on.
    private static void section(String title, Runnable code) {
        System.out.println("=== " + title + " ===");
        try {
            code.run();
        } catch (RuntimeException e) {
            System.out.println("  (not finished yet: " + e + ")");
        }
        System.out.println();
    }
}
