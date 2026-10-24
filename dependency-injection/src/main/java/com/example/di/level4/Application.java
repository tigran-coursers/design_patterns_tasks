package com.example.di.level4;

/**
 * The application: creates a task for every title, and prints the report.
 * Look: it knows NO implementation (no InMemory..., no Console...). It only gets finished objects.
 *
 * PROVIDED CODE - do not change.
 */
public class Application {

    private final TaskService service;
    private final ReportPrinter printer;

    public Application(TaskService service, ReportPrinter printer) {
        this.service = service;
        this.printer = printer;
    }

    public void run(String... titles) {
        for (String title : titles) {
            service.create(title, "team");
        }
        printer.print();
    }
}
