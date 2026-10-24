package com.example.layers.level4.model;

import java.util.Objects;

/** A task. It never changes: "changing" it gives a new Task. PROVIDED CODE - do not change. */
public final class Task {

    private final int id;
    private final String title;
    private final String assignee;
    private final boolean done;

    public Task(int id, String title, String assignee, boolean done) {
        this.id = id;
        this.title = title;
        this.assignee = assignee;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAssignee() {
        return assignee;
    }

    public boolean isDone() {
        return done;
    }

    public Task withId(int newId) {
        return new Task(newId, title, assignee, done);
    }

    public Task withDone(boolean newDone) {
        return new Task(id, title, assignee, newDone);
    }

    public Task withAssignee(String newAssignee) {
        return new Task(id, title, newAssignee, done);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task task)) {
            return false;
        }
        return id == task.id && done == task.done
                && Objects.equals(title, task.title) && Objects.equals(assignee, task.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, assignee, done);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title=" + title + ", assignee=" + assignee + ", done=" + done + "}";
    }
}
