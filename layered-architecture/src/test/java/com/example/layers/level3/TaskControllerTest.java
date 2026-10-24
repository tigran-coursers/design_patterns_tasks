package com.example.layers.level3;

import com.example.layers.level3.presentation.TaskController;
import com.example.layers.level3.presentation.TaskResponse;
import com.example.layers.level3.repository.InMemoryTaskRepository;
import com.example.layers.level3.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Checks Task 3b (the controller gives DTOs, not the model). PROVIDED CODE - do not change.
 *
 * (The variables are typed Object and List<?> on purpose, so this test compiles whatever you return.
 *  The test then checks WHAT it really is.)
 */
class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp() {
        TaskService service = new TaskService(new InMemoryTaskRepository());
        service.create("anna", "Write docs");
        service.create("tom", "Fix bug");
        controller = new TaskController(service);
    }

    @Test
    void get_returnsATaskResponse_notTheModel() {
        Object result = controller.get(1);

        assertInstanceOf(TaskResponse.class, result, "get() must return a TaskResponse (a DTO)");
        assertEquals(new TaskResponse(1, "Write docs", "anna", false), result);
    }

    @Test
    void list_returnsTaskResponses_notTheModel() {
        List<?> result = controller.list();

        assertEquals(2, result.size());
        for (Object element : result) {
            assertInstanceOf(TaskResponse.class, element, "list() must return TaskResponse objects (DTOs)");
        }
        assertEquals(new TaskResponse(2, "Fix bug", "tom", false), result.get(1));
    }

    @Test
    void get_anUnknownTask_isNotFound() {
        assertThrows(NoSuchElementException.class, () -> controller.get(99));
    }
}
