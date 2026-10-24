package com.example.layers.level4.presentation;

import com.example.layers.level4.service.BusinessRuleException;
import com.example.layers.level4.service.NotFoundException;
import com.example.layers.level4.service.TaskService;

/**
 * The text interface (the PRESENTATION layer). It reads the command, calls the service, and writes the answer.
 * It does NOT decide business rules (that is the service). But it must turn every kind of error into a clear answer,
 * so the user never sees a stack trace.
 *
 * STUDENT TASK 4b - the translation of the errors:
 *   TODO 1: (done for you) a number that is not a number  ->  "Error 400: id must be a number"
 *   TODO 2: add the two catch blocks at the bottom of handle().
 */
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    /** Reads one command, calls the service, and writes the answer. Errors become texts "Error <code>: <message>". */
    public String handle(String line) {
        String[] parts = line.trim().split("\\s+", 3);
        try {
            switch (parts[0]) {
                case "add":
                    if (parts.length < 3) {
                        return "Error 400: usage: add <assignee> <title>";
                    }
                    return "Created task " + service.create(parts[1], parts[2]).getId();
                case "done":
                    if (parts.length < 2) {
                        return "Error 400: usage: done <id>";
                    }
                    return "Task " + service.complete(Integer.parseInt(parts[1])).getId() + " done";
                case "list":
                    return TaskFormatter.format(service.findAll());
                default:
                    return "Error 400: unknown command: " + parts[0];
            }
        } catch (NumberFormatException e) {
            return "Error 400: id must be a number";
            // TODO 2: two more catch blocks, to turn the exceptions of the service into texts:
            //   NotFoundException      ->  "Error 404: " + e.getMessage()
            //   BusinessRuleException  ->  "Error 409: " + e.getMessage()
        }
    }
}
