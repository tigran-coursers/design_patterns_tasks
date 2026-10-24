package com.example.layers.level2.repository;

import com.example.layers.level2.model.Task;
import com.example.layers.level2.presentation.TaskFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/** Keeps the tasks in memory. PROVIDED CODE - do not change. */
public class InMemoryTaskRepository implements TaskRepository {

    private final TreeMap<Integer, Task> tasks = new TreeMap<>();
    private int lastId = 0;

    @Override
    public Task save(Task task) {
        Task saved = task;
        if (task.getId() == 0) {
            lastId++;
            saved = task.withId(lastId);
        } else if (task.getId() > lastId) {
            lastId = task.getId();
        }
        tasks.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public String findAllAsText() {                         // TEMPORARY - delete (TODO 1)
        return TaskFormatter.format(findAll());             // the data layer uses a class of the presentation layer!
    }
}
