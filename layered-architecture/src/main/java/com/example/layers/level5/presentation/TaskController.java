package com.example.layers.level5.presentation;

import com.example.layers.level5.service.BusinessRuleException;
import com.example.layers.level5.service.NotFoundException;
import com.example.layers.level5.service.TaskService;

/**
 * The text interface (the PRESENTATION layer).
 *
 * STUDENT TASK 5 - TODO 3: the new command "reassign" (see the case below).
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
                case "reassign":
                    // TODO 3: a new command  "reassign <id> <assignee>"  ->  "Task 1 reassigned to tom"
                    //   - fewer than 3 words (parts.length < 3)  ->  "Error 400: usage: reassign <id> <assignee>"
                    //   - call  service.reassign(Integer.parseInt(parts[1]), parts[2])
                    //   (the catch blocks below already turn the exceptions into "Error 404 / 409 / 400")
                    return "Error 400: unknown command: reassign";   // TEMPORARY - replace it
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
