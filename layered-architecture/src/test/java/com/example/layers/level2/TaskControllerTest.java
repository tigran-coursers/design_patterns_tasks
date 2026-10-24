package com.example.layers.level2;

import com.example.layers.level2.presentation.TaskController;
import com.example.layers.level2.repository.InMemoryTaskRepository;
import com.example.layers.level2.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Level 2 is a REFACTORING: the structure changes, the behavior must NOT change.
 * These tests pass before and after. Run them again when you finish: they prove that you broke nothing.
 * PROVIDED CODE - do not change.
 */
class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(new TaskService(new InMemoryTaskRepository()));
    }

    @Test
    void list_withoutTasks_saysSo() {
        assertEquals("No tasks", controller.handle("list"));
    }

    @Test
    void add_createsATask() {
        assertEquals("Created task 1", controller.handle("add anna Write docs"));
        assertEquals("Created task 2", controller.handle("add tom Fix bug"));
    }

    @Test
    void list_showsTheTasks() {
        controller.handle("add anna Write docs");
        controller.handle("add tom Fix bug");

        assertEquals("1 [ ] Write docs (anna)\n2 [ ] Fix bug (tom)", controller.handle("list"));
    }

    @Test
    void anUnknownCommand_isAnError() {
        assertEquals("Error: unknown command: xyz", controller.handle("xyz"));
    }
}
