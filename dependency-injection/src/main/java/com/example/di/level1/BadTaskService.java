package com.example.di.level1;

import com.example.di.domain.Task;
import com.example.di.impl.ConsoleNotifier;
import com.example.di.impl.InMemoryTaskRepository;

/**
 * THE BAD EXAMPLE - read it, do not change it.
 *
 * The service creates its own helpers with "new". It works, but:
 *   - it is welded to ONE repository and ONE notifier. To use a file or an email, you must open and change it.
 *   - you cannot test it: every test prints to the console and uses a real repository.
 *   - the dependencies are HIDDEN. Looking at "new BadTaskService()", nobody knows what it needs.
 *
 * Your task: the same service, but the helpers are GIVEN to it from outside (Dependency Injection).
 */
public class BadTaskService {

    private final InMemoryTaskRepository repository = new InMemoryTaskRepository();
    private final ConsoleNotifier notifier = new ConsoleNotifier();

    public Task create(String title, String assignee) {
        Task saved = repository.save(new Task(0, title, assignee, false));
        notifier.send(assignee, "New task: " + title);
        return saved;
    }
}
