package com.example.di.level7;

/**
 * Prints a short report. It needs a TaskService.
 *
 * STUDENT TASK 7b - mark it as a component, so Spring finds it by itself (component scan).
 *
 *   TODO: add the annotation   @Component   above the class   (org.springframework.stereotype.Component)
 *
 * With one constructor, Spring understands by itself that it must give it a TaskService. No @Autowired needed.
 */
public class ReportPrinter {                    // TODO 7b: @Component

    private final TaskService service;

    public ReportPrinter(TaskService service) {
        this.service = service;
    }

    public TaskService getService() {
        return service;
    }

    public String report() {
        return "Tasks: " + service.findAll().size();
    }
}
