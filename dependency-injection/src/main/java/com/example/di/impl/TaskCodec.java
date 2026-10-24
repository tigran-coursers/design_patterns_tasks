package com.example.di.impl;

import com.example.di.domain.Task;

/**
 * Turns a Task into one line of text and back. Used by FileTaskRepository.
 * The line looks like:   3;Write docs;anna;false     (id;title;assignee;done)
 * The title and the assignee must not contain a ";".
 *
 * PROVIDED CODE - do not change.
 */
public final class TaskCodec {

    private TaskCodec() {
    }

    public static String toLine(Task task) {
        return task.getId() + ";" + task.getTitle() + ";" + task.getAssignee() + ";" + task.isDone();
    }

    public static Task fromLine(String line) {
        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Bad task line: " + line);
        }
        return new Task(Integer.parseInt(parts[0]), parts[1], parts[2], Boolean.parseBoolean(parts[3]));
    }
}
