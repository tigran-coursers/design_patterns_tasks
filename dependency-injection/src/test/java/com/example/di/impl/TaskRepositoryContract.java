package com.example.di.impl;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * THE CONTRACT of a TaskRepository: the tests that EVERY implementation must pass.
 * This is what makes implementations swappable: the program can use any of them, because they all behave the same.
 * It is run once for InMemoryTaskRepository, and once for FileTaskRepository (your task 3a).
 *
 * PROVIDED CODE - do not change.
 */
abstract class TaskRepositoryContract {

    protected TaskRepository repository;

    protected abstract TaskRepository createRepository();

    @BeforeEach
    void setUpRepository() {
        repository = createRepository();
    }

    @Test
    void findAll_startsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void save_aNewTask_getsTheIds1And2() {
        Task first = repository.save(new Task(0, "Write docs", "anna", false));
        Task second = repository.save(new Task(0, "Fix bug", "tom", false));

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
    }

    @Test
    void save_returnsTheTaskWithTheSameData() {
        Task saved = repository.save(new Task(0, "Write docs", "anna", false));

        assertEquals(new Task(1, "Write docs", "anna", false), saved);
    }

    @Test
    void findById_findsASavedTask() {
        Task saved = repository.save(new Task(0, "Write docs", "anna", false));

        assertEquals(saved, repository.findById(saved.getId()).orElseThrow());
    }

    @Test
    void findById_unknownId_isEmpty() {
        assertTrue(repository.findById(99).isEmpty());
    }

    @Test
    void findAll_returnsTheTasksOrderedById() {
        repository.save(new Task(0, "A", "anna", false));
        repository.save(new Task(0, "B", "tom", true));
        repository.save(new Task(0, "C", "anna", false));

        List<Task> all = repository.findAll();

        assertEquals(3, all.size());
        assertEquals("A", all.get(0).getTitle());
        assertEquals("B", all.get(1).getTitle());
        assertEquals("C", all.get(2).getTitle());
    }

    @Test
    void save_aTaskWithAnExistingId_replacesIt() {
        Task saved = repository.save(new Task(0, "Write docs", "anna", false));

        repository.save(saved.withDone(true));

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(saved.getId()).orElseThrow().isDone());
    }

    @Test
    void save_afterAReplace_theNextNewIdIsStillNew() {
        Task first = repository.save(new Task(0, "A", "anna", false));
        repository.save(first.withDone(true));

        Task second = repository.save(new Task(0, "B", "anna", false));

        assertEquals(2, second.getId());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findAll_returnsACopy() {
        repository.save(new Task(0, "A", "anna", false));

        repository.findAll().clear();

        assertEquals(1, repository.findAll().size());
    }
}
