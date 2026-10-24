package com.example.di.level4.wiring;

import com.example.di.level4.Application;

import java.io.PrintStream;

/**
 * THE COMPOSITION ROOT: the ONE place of the program that knows all the implementations
 * and connects the objects. Everywhere else you only receive objects. Nobody else says "new".
 *
 * The picture to build (read it from the bottom):
 *
 *     Application
 *       |-- TaskService ---- InMemoryTaskRepository
 *       |        `---------- ConsoleNotifier(out)
 *       `-- ReportPrinter(the SAME TaskService, out)
 *
 * STUDENT TASK 4 - write build().
 *
 *   TODO:
 *     1. create the repository:   new InMemoryTaskRepository()            (package com.example.di.impl)
 *     2. create the notifier:     new ConsoleNotifier(out)                (print to the stream that was given!)
 *     3. create ONE TaskService with the two objects above
 *     4. create the ReportPrinter with that SAME TaskService (and out)
 *     5. create the Application with the service and the printer, and return it.
 *
 * Why "the SAME"? If you create a second TaskService for the printer, it has its own empty repository.
 * The report would say "Tasks: 0". Two objects must SHARE one instance. That is what the composition root decides.
 */
public final class CompositionRoot {

    private CompositionRoot() {
    }

    public static Application build(PrintStream out) {
        // TODO

        return null;    // TEMPORARY - delete this line
    }
}
