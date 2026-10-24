package com.example.layers.level6.presentation;

import com.example.layers.level6.service.BusinessRuleException;
import com.example.layers.level6.service.NotFoundException;
import com.example.layers.level6.service.TaskService;

/**
 * The text interface (the PRESENTATION layer). It is FINISHED, and it never changes in this level.
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
        } catch (NotFoundException e) {
            return "Error 404: " + e.getMessage();
        } catch (BusinessRuleException e) {
            return "Error 409: " + e.getMessage();
        }
    }
}
