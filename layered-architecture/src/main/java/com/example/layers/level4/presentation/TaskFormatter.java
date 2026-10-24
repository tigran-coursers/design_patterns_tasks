package com.example.layers.level4.presentation;

import com.example.layers.level4.model.Task;

import java.util.List;

/** Turns the tasks into the text for the screen (a PRESENTATION job). PROVIDED CODE - do not change. */
public final class TaskFormatter {

    private TaskFormatter() {
    }

    public static String format(List<Task> tasks) {
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
    }
}
