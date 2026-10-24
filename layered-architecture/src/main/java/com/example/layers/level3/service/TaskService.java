package com.example.layers.level3.service;

import com.example.layers.level3.model.Task;
import com.example.layers.level3.repository.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;

/** The rules of the application (the SERVICE layer). It works with the MODEL (Task). PROVIDED CODE - do not change. */
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(String assignee, String title) {
        return repository.save(new Task(0, title, assignee, false, "created by the system"));
    }

    public Task findById(int id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("task " + id + " not found"));
    }

    public List<Task> findAll() {
        return repository.findAll();
    }
}
