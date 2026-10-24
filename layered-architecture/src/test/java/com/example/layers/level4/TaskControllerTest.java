package com.example.layers.level4;

import com.example.layers.level4.presentation.TaskController;
import com.example.layers.level4.repository.InMemoryTaskRepository;
import com.example.layers.level4.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks Task 4b (the controller turns every error into a clear answer). PROVIDED CODE - do not change.
 * The user must NEVER see an exception: every problem becomes a line "Error <code>: <message>".
 */
class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(new TaskService(new InMemoryTaskRepository()));
    }

    @Test
    void add_andDone_andList_work() {
        assertEquals("Created task 1", controller.handle("add anna Write docs"));
        assertEquals("Task 1 done", controller.handle("done 1"));
        assertEquals("1 [x] Write docs (anna)", controller.handle("list"));
    }

    @Test
    void list_withoutTasks() {
        assertEquals("No tasks", controller.handle("list"));
    }

    @Test
    void add_withoutATitle_is400() {
        assertEquals("Error 400: usage: add <assignee> <title>", controller.handle("add anna"));
    }

    @Test
    void done_withoutAnId_is400() {
        assertEquals("Error 400: usage: done <id>", controller.handle("done"));
    }

    @Test
    void done_withATextThatIsNotANumber_is400() {
        assertEquals("Error 400: id must be a number", controller.handle("done abc"));
    }

    @Test
    void aUnknownCommand_is400() {
        assertEquals("Error 400: unknown command: xyz", controller.handle("xyz"));
    }

    @Test
    void done_anUnknownTask_is404() {
        assertEquals("Error 404: task 99 not found", controller.handle("done 99"));
    }

    @Test
    void done_twice_is409() {
        controller.handle("add anna Write docs");
        controller.handle("done 1");

        assertEquals("Error 409: task 1 is already done", controller.handle("done 1"));
    }

    @Test
    void add_aFourthOpenTask_is409() {
        controller.handle("add anna One");
        controller.handle("add anna Two");
        controller.handle("add anna Three");

        assertEquals("Error 409: anna already has 3 open tasks", controller.handle("add anna Four"));
    }
}
