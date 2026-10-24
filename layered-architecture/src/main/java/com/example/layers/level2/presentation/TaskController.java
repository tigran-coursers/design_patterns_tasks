package com.example.layers.level2.presentation;

import com.example.layers.level2.model.Task;
import com.example.layers.level2.service.TaskService;

/**
 * The text interface (the PRESENTATION layer).
 *
 * STUDENT TASK 2 - the "list" command jumps over the service, and asks the data layer directly:
 *
 *     presentation  ---X--->  repository       (forbidden: the presentation layer talks only to the service)
 *
 * TODO 3: in "list", use the service:   service.findAll()   and format the result:   TaskFormatter.format(...)
 */
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    public String handle(String line) {
        String[] parts = line.trim().split("\\s+", 3);
        switch (parts[0]) {
            case "add":
                Task created = service.create(parts[1], parts[2]);
                return "Created task " + created.getId();
            case "list":
                return service.getRepository().findAllAsText();     // TEMPORARY - TODO 3
            default:
                return "Error: unknown command: " + parts[0];
        }
    }
}
