package com.example.di.level7;

import com.example.di.domain.Notifier;
import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;

import java.util.List;

/** A service that gets its helpers from outside. No annotations: it knows nothing about Spring. PROVIDED CODE - do not change. */
public class TaskService {

    private final TaskRepository repository;
    private final Notifier notifier;

    public TaskService(TaskRepository repository, Notifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public Task create(String title, String assignee) {
        Task saved = repository.save(new Task(0, title, assignee, false));
        notifier.send(assignee, "New task: " + title);
        return saved;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }
}
