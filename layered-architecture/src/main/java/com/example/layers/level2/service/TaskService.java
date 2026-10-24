package com.example.layers.level2.service;

import com.example.layers.level2.model.Task;
import com.example.layers.level2.repository.TaskRepository;

import java.util.List;

/**
 * The rules of the application (the SERVICE layer).
 *
 * STUDENT TASK 2 (with TaskController, TaskFormatter, TaskRepository and InMemoryTaskRepository):
 * the code works, but TWO rules of the layers are broken. Find them with the ArchUnit test (Level2ArchTest)
 * and fix them. The behavior must stay the same: the tests of the "list" command must stay green.
 */
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(String assignee, String title) {
        return repository.save(new Task(0, title, assignee, false));
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    /** TEMPORARY - it gives the data layer to the presentation layer, so it can jump over the service (TODO 2). Delete it. */
    public TaskRepository getRepository() {
        return repository;
    }
}
