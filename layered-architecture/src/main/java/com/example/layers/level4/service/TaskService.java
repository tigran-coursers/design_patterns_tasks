package com.example.layers.level4.service;

import com.example.layers.level4.model.Task;
import com.example.layers.level4.repository.TaskRepository;

import java.util.List;

/**
 * The rules of the application (the SERVICE layer).
 *
 * STUDENT TASK 4a - the business rules. A rule belongs HERE, not in the controller and not in the repository.
 * Throw these exceptions (they are in this package):
 *
 *   create(assignee, title):
 *     - the title is null or blank           ->  BusinessRuleException("title is required")
 *     - the person has MAX_OPEN_TASKS (3) open tasks already  ->  BusinessRuleException(assignee + " already has 3 open tasks")
 *       (use the method countOpen below. The 3rd task is allowed, the 4th is not)
 *   complete(id):
 *     - there is no such task                ->  NotFoundException("task " + id + " not found")
 *     - the task is done already             ->  BusinessRuleException("task " + id + " is already done")

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
        // TODO: the two rules of create (see above), then save the new task
        return repository.save(new Task(0, title, assignee, false));        // TEMPORARY - no rules yet
    }

    public Task complete(int id) {
        // TODO: the two rules of complete (see above), then save the task as done
        Task task = repository.findById(id).orElseThrow();                   // TEMPORARY - no rules yet
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
