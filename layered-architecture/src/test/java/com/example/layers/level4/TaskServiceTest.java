package com.example.layers.level4;

import com.example.layers.level4.model.Task;
import com.example.layers.level4.repository.InMemoryTaskRepository;
import com.example.layers.level4.service.BusinessRuleException;
import com.example.layers.level4.service.NotFoundException;
import com.example.layers.level4.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks Task 4a (the business rules live in the SERVICE). PROVIDED CODE - do not change. */
class TaskServiceTest {

    private InMemoryTaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        service = new TaskService(repository);
    }

    @Test
    void create_savesAndReturnsTheTask() {
        Task task = service.create("anna", "Write docs");

        assertEquals(new Task(1, "Write docs", "anna", false), task);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void create_aBlankTitle_isRefused_andNothingIsSaved() {
        BusinessRuleException error = assertThrows(BusinessRuleException.class, () -> service.create("anna", "   "));

        assertEquals("title is required", error.getMessage());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void create_aNullTitle_isRefused() {
        assertThrows(BusinessRuleException.class, () -> service.create("anna", null));
    }

    @Test
    void create_theThirdOpenTask_isAllowed_theFourthIsNot() {
        service.create("anna", "One");
        service.create("anna", "Two");
        service.create("anna", "Three");

        BusinessRuleException error = assertThrows(BusinessRuleException.class, () -> service.create("anna", "Four"));

        assertEquals("anna already has 3 open tasks", error.getMessage());
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void create_theLimitIsPerPerson() {
        service.create("anna", "One");
        service.create("anna", "Two");
        service.create("anna", "Three");

        assertEquals("tom", service.create("tom", "Mine").getAssignee());
    }

    @Test
    void create_aDoneTaskDoesNotCountForTheLimit() {
        service.create("anna", "One");
        service.create("anna", "Two");
        Task three = service.create("anna", "Three");
        service.complete(three.getId());

        assertEquals("Four", service.create("anna", "Four").getTitle());
    }

    @Test
    void complete_marksTheTaskDone_andSavesIt() {
        Task created = service.create("anna", "Write docs");

        Task done = service.complete(created.getId());

        assertTrue(done.isDone());
        assertTrue(repository.findById(created.getId()).orElseThrow().isDone());
    }

    @Test
    void complete_anUnknownTask_isNotFound() {
        NotFoundException error = assertThrows(NotFoundException.class, () -> service.complete(99));

        assertEquals("task 99 not found", error.getMessage());
    }

    @Test
    void complete_aTaskThatIsDoneAlready_isRefused() {
        Task created = service.create("anna", "Write docs");
        service.complete(created.getId());

        BusinessRuleException error = assertThrows(BusinessRuleException.class, () -> service.complete(created.getId()));

        assertEquals("task 1 is already done", error.getMessage());
    }
}
