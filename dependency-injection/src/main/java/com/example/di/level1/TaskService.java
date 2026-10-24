package com.example.di.level1;

import com.example.di.domain.Notifier;
import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;
import com.example.di.impl.ConsoleNotifier;
import com.example.di.impl.InMemoryTaskRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The same service as BadTaskService, but built the RIGHT way: it does not choose its helpers.
 *
 * STUDENT TASK 1 - constructor injection.
 *
 *   TODO 1: change the TYPES of the two fields to the INTERFACES  TaskRepository  and  Notifier
 *           (the service must not even know that InMemoryTaskRepository and ConsoleNotifier exist).
 *   TODO 2: do NOT create them with "new" any more. Remove the "= new ..." parts.
 *   TODO 3: in the constructor, save the two objects that were GIVEN:   this.repository = repository;  ...
 *   TODO 4: remove the two imports at the top that you do not need any more (impl.*).
 *
 * The rest of the class already works. Do not change it.
 */
public class TaskService {

    private final InMemoryTaskRepository repository = new InMemoryTaskRepository();   // TEMPORARY - see TODO 1 and 2
    private final ConsoleNotifier notifier = new ConsoleNotifier();                   // TEMPORARY - see TODO 1 and 2

    public TaskService(TaskRepository repository, Notifier notifier) {
        // TODO 3: save the repository and the notifier that were given
    }

    // ---------------------------------------------------------------- PROVIDED - these already work

    /** Saves a new task (not done) and tells the assignee: "New task: <title>". Returns the saved task. */
    public Task create(String title, String assignee) {
        Task saved = repository.save(new Task(0, title, assignee, false));
        notifier.send(assignee, "New task: " + title);
        return saved;
    }

    /** Marks the task as done and tells the assignee: "Task done: <title>". Throws NoSuchElementException("Task not found: <id>"). */
    public Task complete(int id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        Task saved = repository.save(task.withDone(true));
        notifier.send(task.getAssignee(), "Task done: " + task.getTitle());
        return saved;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }
}
