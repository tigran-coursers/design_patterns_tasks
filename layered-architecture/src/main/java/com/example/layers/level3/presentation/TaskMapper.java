package com.example.layers.level3.presentation;

import com.example.layers.level3.model.Task;

import java.util.List;

/**
 * Turns the MODEL into the DTO. It is the only class of the presentation layer that may touch the model.
 *
 * STUDENT TASK 3a - write the two methods.
 */
public final class TaskMapper {

    private TaskMapper() {
    }

    /** One Task -> one TaskResponse (the same id, title, assignee and done). */
    public static TaskResponse toResponse(Task task) {
        // TODO: return new TaskResponse(...), with the values of the task

        throw new UnsupportedOperationException("TODO: toResponse");   // TEMPORARY - delete this line
    }

    /** A list of tasks -> a NEW list of responses, in the same order. An empty list gives an empty list. */
    public static List<TaskResponse> toResponses(List<Task> tasks) {
        // TODO: a new ArrayList, call toResponse for every task, return the list

        throw new UnsupportedOperationException("TODO: toResponses");   // TEMPORARY - delete this line
    }
}
