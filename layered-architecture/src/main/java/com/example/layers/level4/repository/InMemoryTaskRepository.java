package com.example.layers.level4.repository;

import com.example.layers.level4.model.Task;

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
}
