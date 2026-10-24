package com.example.layers.level6.repository;

import com.example.layers.level6.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * A repository that WRAPS another repository, and writes a line in an audit log for every call.
 * (It is the DECORATOR pattern. You wrote one in the module "structural-patterns".)
 *
 * The service does not know about it. You can add it, or remove it, and nothing else changes.
 * That is the reward of good layers: you can replace or wrap a layer.
 *
 * STUDENT TASK 6 - write the three methods. Each one DELEGATES to the other repository, and adds ONE line to the log:
 *
 *     save(task)       ->  call delegate.save(task), then log  "save " + (the id of the SAVED task)   and return the saved task
 *     findAll()        ->  log "findAll", and return delegate.findAll()
 *     findById(id)     ->  log "findById " + id, and return delegate.findById(id)
 */
public class AuditingTaskRepository implements TaskRepository {

    private final TaskRepository delegate;
    private final List<String> log;

    public AuditingTaskRepository(TaskRepository delegate, List<String> log) {
        this.delegate = delegate;
        this.log = log;
    }

    @Override
    public Task save(Task task) {
        // TODO
        throw new UnsupportedOperationException("TODO: save");        // TEMPORARY - delete this line
    }

    @Override
    public List<Task> findAll() {
        // TODO
        throw new UnsupportedOperationException("TODO: findAll");     // TEMPORARY - delete this line
    }

    @Override
    public Optional<Task> findById(int id) {
        // TODO
        throw new UnsupportedOperationException("TODO: findById");    // TEMPORARY - delete this line
    }
}
