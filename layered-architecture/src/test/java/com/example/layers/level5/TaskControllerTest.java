package com.example.layers.level5;

import com.example.layers.level5.presentation.TaskController;
import com.example.layers.level5.repository.InMemoryTaskRepository;
import com.example.layers.level5.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Checks Task 5, TODO 3 (the new command in the PRESENTATION layer). PROVIDED CODE - do not change. */
class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(new TaskService(new InMemoryTaskRepository()));
    }

    @Test
    void theOldCommandsStillWork() {
        assertEquals("Created task 1", controller.handle("add anna Write docs"));
        assertEquals("Task 1 done", controller.handle("done 1"));
        assertEquals("Error 404: task 99 not found", controller.handle("done 99"));
    }

    @Test
    void reassign_movesTheTask() {
        controller.handle("add anna Write docs");

        assertEquals("Task 1 reassigned to tom", controller.handle("reassign 1 tom"));
        assertEquals("1 [ ] Write docs (tom)", controller.handle("list"));
    }

    @Test
    void reassign_withoutEnoughWords_is400() {
        assertEquals("Error 400: usage: reassign <id> <assignee>", controller.handle("reassign 1"));
        assertEquals("Error 400: usage: reassign <id> <assignee>", controller.handle("reassign"));
    }

    @Test
    void reassign_withATextThatIsNotANumber_is400() {
        assertEquals("Error 400: id must be a number", controller.handle("reassign abc tom"));
    }

    @Test
    void reassign_anUnknownTask_is404() {
        assertEquals("Error 404: task 99 not found", controller.handle("reassign 99 tom"));
    }

    @Test
    void reassign_aDoneTask_is409() {
        controller.handle("add anna Write docs");
        controller.handle("done 1");

        assertEquals("Error 409: task 1 is done and cannot be reassigned", controller.handle("reassign 1 tom"));
    }

    @Test
    void reassign_toAPersonAtTheLimit_is409() {
        controller.handle("add tom One");
        controller.handle("add tom Two");
        controller.handle("add tom Three");
        controller.handle("add anna Mine");

        assertEquals("Error 409: tom already has 3 open tasks", controller.handle("reassign 4 tom"));
    }
}
