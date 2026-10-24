package com.example.di.level5;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Answers questions about the tasks.
 *
 * STUDENT TASK 5 - remove the Service Locator, and use constructor injection.
 *
 *   TODO 1: add a field:   private final TaskRepository repository;
 *   TODO 2: in the constructor, save the repository that was given.
 *   TODO 3: in both methods, use  this.repository  and delete the line with  ServiceLocator.get(...).
 *   (At the end, no line of this class mentions ServiceLocator.)
 */
public class ReportService {

    public ReportService(TaskRepository repository) {
        // TODO 2: save the repository
    }

    /** How many tasks are NOT done. */
    public long countOpen() {
        TaskRepository repository = ServiceLocator.get(TaskRepository.class);   // TEMPORARY - TODO 3
        long open = 0;
        for (Task task : repository.findAll()) {
            if (!task.isDone()) {
                open++;
            }
        }
        return open;
    }

    /** The titles of the tasks of this person, in the order of the repository. */
    public List<String> titlesFor(String assignee) {
        TaskRepository repository = ServiceLocator.get(TaskRepository.class);   // TEMPORARY - TODO 3
        List<String> titles = new ArrayList<>();
        for (Task task : repository.findAll()) {
            if (task.getAssignee().equals(assignee)) {
                titles.add(task.getTitle());
            }
        }
        return titles;
    }
}
