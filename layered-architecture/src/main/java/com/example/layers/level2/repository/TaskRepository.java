package com.example.layers.level2.repository;

import com.example.layers.level2.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * The DATA layer: where tasks are kept.
 * save(task) with id 0 gives a NEW id (1, 2, 3 ...). save(task) with an existing id REPLACES that task.
 * findAll() returns the tasks ordered by id, as a COPY.
 *
 * PROVIDED CODE - do not change.
 */
public interface TaskRepository {

    Task save(Task task);

    List<Task> findAll();

    Optional<Task> findById(int id);

    /** TEMPORARY - a text for the screen. This is NOT a job of the data layer. */
    String findAllAsText();
}
