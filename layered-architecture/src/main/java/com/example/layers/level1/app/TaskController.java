package com.example.layers.level1.app;

import java.util.List;

/** The text interface (the PRESENTATION layer): reads a command, calls the service, writes the answer. */
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
            case "done":
                Task done = service.complete(Integer.parseInt(parts[1]));
                return "Task " + done.getId() + " done";
            case "list":
                List<Task> tasks = service.findAll();
                if (tasks.isEmpty()) {
                    return "No tasks";
                }
                StringBuilder text = new StringBuilder();
                for (Task task : tasks) {
                    if (text.length() > 0) {
                        text.append("\n");
                    }
                    text.append(task.getId()).append(" [").append(task.isDone() ? "x" : " ").append("] ")
                            .append(task.getTitle()).append(" (").append(task.getAssignee()).append(")");
                }
                return text.toString();
            default:
                return "Error: unknown command: " + parts[0];
        }
    }
}
