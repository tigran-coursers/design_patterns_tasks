package com.example.di.domain;

import java.util.List;
import java.util.Optional;

/**
 * Where tasks are kept. An INTERFACE: the rest of the program does not know (and must not care)
 * if the tasks are in memory, in a file, or in a database.
 *
 * The rules of every implementation:
 *   - save(task) with id 0 gives the task a NEW id: 1, then 2, then 3 ... and returns the saved task.
 *   - save(task) with an id that exists REPLACES that task.
 *   - findAll() returns the tasks ordered by id. It is a COPY: changing the list changes nothing.
 *   - findById(id) returns the task, or Optional.empty().
 *
 * PROVIDED CODE - do not change.
 */
public interface TaskRepository {

    Task save(Task task);

    List<Task> findAll();

    Optional<Task> findById(int id);
}
