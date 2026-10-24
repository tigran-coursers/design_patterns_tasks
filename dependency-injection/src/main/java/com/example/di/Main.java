package com.example.di;

import com.example.di.domain.Task;
import com.example.di.impl.ConsoleNotifier;
import com.example.di.impl.InMemoryTaskRepository;
import com.example.di.impl.RecordingNotifier;
import com.example.di.level1.TaskService;
import com.example.di.level2.WelcomeService;
import com.example.di.level3.DeadlineService;
import com.example.di.level4.wiring.CompositionRoot;
import com.example.di.level5.ReportService;
import com.example.di.level6.Container;
import com.example.di.level7.AppConfig;
import com.example.di.domain.Notifier;
import com.example.di.domain.TaskRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Shows all seven levels working. Run it to SEE your code.
 * (The tests are the real check. This is the "look at it" version.)
 *
 * PROVIDED CODE - do not change.
 */
public class Main {

    public static void main(String[] args) {
        section("Level 1 - constructor injection", () -> {
            InMemoryTaskRepository repository = new InMemoryTaskRepository();
            TaskService service = new TaskService(repository, (to, message) -> System.out.println("  fake notifier -> " + to + ": " + message));
            service.create("Write docs", "anna");
            System.out.println("  tasks in MY repository: " + repository.findAll().size() + "   (must be 1)");
            System.out.println("  (must also print: fake notifier -> anna: New task: Write docs)");
        });

        section("Level 2 - never half-built", () -> {
            new WelcomeService(new ConsoleNotifier()).welcome("tom");
            try {
                new WelcomeService(null);
                System.out.println("  null was accepted   (must NOT happen)");
            } catch (NullPointerException e) {
                System.out.println("  null refused: " + e.getMessage() + "   (must be: notifier is required)");
            }
        });

        section("Level 3 - the clock is a dependency", () -> {
            Clock clock = Clock.fixed(Instant.parse("2020-06-15T10:00:00Z"), ZoneOffset.UTC);
            DeadlineService deadlines = new DeadlineService(clock);
            System.out.println("  overdue 2020-06-14: " + deadlines.isOverdue(LocalDate.of(2020, 6, 14)) + "   (must be true)");
            System.out.println("  overdue 2020-06-15: " + deadlines.isOverdue(LocalDate.of(2020, 6, 15)) + "   (must be false)");
            System.out.println("  days left to 2020-06-20: " + deadlines.daysLeft(LocalDate.of(2020, 6, 20)) + "   (must be 5)");
        });

        section("Level 4 - the composition root", () -> {
            CompositionRoot.build(System.out).run("Write docs", "Fix bug");
            System.out.println("  (must end with: Tasks: 2, and two lines of tasks)");
        });

        section("Level 5 - no service locator", () -> {
            InMemoryTaskRepository repository = new InMemoryTaskRepository();
            repository.save(new Task(0, "A", "anna", false));
            repository.save(new Task(0, "B", "anna", true));
            ReportService report = new ReportService(repository);
            System.out.println("  open tasks: " + report.countOpen() + "   (must be 1)");
            System.out.println("  titles of anna: " + report.titlesFor("anna") + "   (must be [A, B])");
        });

        section("Level 6 - a tiny container", () -> {
            Container container = new Container();
            container.register(TaskRepository.class, InMemoryTaskRepository.class);
            container.register(Notifier.class, RecordingNotifier.class);
            container.register(com.example.di.level4.TaskService.class);
            com.example.di.level4.TaskService first = container.get(com.example.di.level4.TaskService.class);
            com.example.di.level4.TaskService second = container.get(com.example.di.level4.TaskService.class);
            System.out.println("  built: " + (first != null) + ", same object twice: " + (first != null && first == second) + "   (must be: built: true, same object twice: true)");
        });

        section("Level 7 - the same, with Spring", () -> {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
                com.example.di.level7.TaskService service = context.getBean(com.example.di.level7.TaskService.class);
                service.create("Learn Spring", "anna");
                System.out.println("  " + context.getBean(com.example.di.level7.ReportPrinter.class).report() + "   (must be: Tasks: 1)");
            }
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
