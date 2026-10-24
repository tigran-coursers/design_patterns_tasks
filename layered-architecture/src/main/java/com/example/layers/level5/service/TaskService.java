package com.example.layers.level5.service;

import com.example.layers.level5.model.Task;
import com.example.layers.level5.repository.TaskRepository;

import java.util.List;

/**
 * The rules of the application (the SERVICE layer).
 *
 * STUDENT TASK 5 - add a NEW FEATURE through all the layers: reassign a task to another person.
 *   TODO 1: write reassign(id, newAssignee) below. The rules, in this order:
 *     - there is no such task        ->  NotFoundException("task " + id + " not found")
 *     - the task is done             ->  BusinessRuleException("task " + id + " is done and cannot be reassigned")
 *     - the new person has MAX_OPEN_TASKS open tasks already  ->  BusinessRuleException(newAssignee + " already has 3 open tasks")
 *     - otherwise save the task with the new assignee (task.withAssignee(...)) and return the saved task
 *   TODO 2 (in the controller): the command "reassign <id> <assignee>".
 *   (The repository needs NO change: save() already replaces a task. A good layer is easy to extend.)
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

    public Task reassign(int id, String newAssignee) {
        // TODO 1
        throw new UnsupportedOperationException("TODO: reassign");           // TEMPORARY - delete this line
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
