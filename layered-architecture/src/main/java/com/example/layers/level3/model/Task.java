package com.example.layers.level3.model;

import java.util.Objects;

/** A task. It never changes: "changing" it gives a new Task. PROVIDED CODE - do not change. */
public final class Task {

    private final int id;
    private final String title;
    private final String assignee;
    private final boolean done;
    private final String internalNote;

    public Task(int id, String title, String assignee, boolean done, String internalNote) {
        this.id = id;
        this.title = title;
        this.assignee = assignee;
        this.done = done;
        this.internalNote = internalNote;
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

    /** Internal information. It is for the system, NOT for the screen. */
    public String getInternalNote() {
        return internalNote;
    }

    public Task withId(int newId) {
        return new Task(newId, title, assignee, done, internalNote);
    }

    public Task withDone(boolean newDone) {
        return new Task(id, title, assignee, newDone, internalNote);
    }

    public Task withAssignee(String newAssignee) {
        return new Task(id, title, newAssignee, done, internalNote);
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
                && Objects.equals(title, task.title) && Objects.equals(assignee, task.assignee)
                && Objects.equals(internalNote, task.internalNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, assignee, done, internalNote);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title=" + title + ", assignee=" + assignee + ", done=" + done + "}";
    }
}
