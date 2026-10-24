package com.example.di.level4;

import com.example.di.domain.Task;

import java.io.PrintStream;

/** Prints a report of all the tasks. It uses the SAME TaskService as everybody else. PROVIDED CODE - do not change. */
public class ReportPrinter {

    private final TaskService service;
    private final PrintStream out;

    public ReportPrinter(TaskService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    public void print() {
        out.println("Tasks: " + service.findAll().size());
        for (Task task : service.findAll()) {
            out.println("  - [" + (task.isDone() ? "x" : " ") + "] " + task.getTitle() + " (" + task.getAssignee() + ")");
        }
    }
}
