package com.example.layers.level1.app;

import java.util.List;
import java.util.NoSuchElementException;

/** The rules of the application (the SERVICE layer). */
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(String assignee, String title) {
        return repository.save(new Task(0, title, assignee, false));
    }

    public Task complete(int id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("task " + id + " not found"));
        return repository.save(task.withDone(true));
    }

    public List<Task> findAll() {
        return repository.findAll();
    }
}
