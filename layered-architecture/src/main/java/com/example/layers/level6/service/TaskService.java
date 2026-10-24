package com.example.layers.level6.service;

import com.example.layers.level6.model.Task;
import com.example.layers.level6.repository.TaskRepository;

import java.util.List;

/**
 * The rules of the application (the SERVICE layer). It is FINISHED, and it never changes in this level.
 */
public class TaskService {

    /** A person can have at most this many OPEN (not done) tasks. */
    public static final int MAX_OPEN_TASKS = 3;

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task create(String assignee, String title) {
        if (title == null || title.isBlank()) {
            throw new BusinessRuleException("title is required");
        }
        if (countOpen(assignee) >= MAX_OPEN_TASKS) {
            throw new BusinessRuleException(assignee + " already has " + MAX_OPEN_TASKS + " open tasks");
        }
        return repository.save(new Task(0, title, assignee, false));
    }

    public Task complete(int id) {
        Task task = repository.findById(id).orElseThrow(() -> new NotFoundException("task " + id + " not found"));
        if (task.isDone()) {
            throw new BusinessRuleException("task " + id + " is already done");
        }
        return repository.save(task.withDone(true));
    }

    private long countOpen(String assignee) {
        long open = 0;
        for (Task task : repository.findAll()) {
            if (task.getAssignee().equals(assignee) && !task.isDone()) {
                open++;
            }
        }
        return open;
    }
}
