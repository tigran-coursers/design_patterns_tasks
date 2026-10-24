package com.example.layers.level5;

import com.example.layers.level5.model.Task;
import com.example.layers.level5.repository.InMemoryTaskRepository;
import com.example.layers.level5.service.BusinessRuleException;
import com.example.layers.level5.service.NotFoundException;
import com.example.layers.level5.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Checks Task 5, TODO 1 (the new rule in the SERVICE). PROVIDED CODE - do not change. */
class TaskServiceTest {

    private InMemoryTaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        service = new TaskService(repository);
    }

    @Test
    void theOldRulesStillWork() {
        service.create("anna", "Write docs");
        assertEquals("task 99 not found", assertThrows(NotFoundException.class, () -> service.complete(99)).getMessage());
    }

    @Test
    void reassign_changesTheAssignee_andKeepsTheRest() {
        Task created = service.create("anna", "Write docs");

        Task moved = service.reassign(created.getId(), "tom");

        assertEquals(new Task(1, "Write docs", "tom", false), moved);
        assertEquals("tom", repository.findById(1).orElseThrow().getAssignee());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void reassign_anUnknownTask_isNotFound() {
        NotFoundException error = assertThrows(NotFoundException.class, () -> service.reassign(99, "tom"));

        assertEquals("task 99 not found", error.getMessage());
    }

    @Test
    void reassign_aDoneTask_isRefused() {
        Task created = service.create("anna", "Write docs");
        service.complete(created.getId());

        BusinessRuleException error = assertThrows(BusinessRuleException.class, () -> service.reassign(created.getId(), "tom"));

        assertEquals("task 1 is done and cannot be reassigned", error.getMessage());
        assertEquals("anna", repository.findById(1).orElseThrow().getAssignee());
    }

    @Test
    void reassign_toAPersonWhoHasThreeOpenTasks_isRefused() {
        service.create("tom", "One");
        service.create("tom", "Two");
        service.create("tom", "Three");
        Task mine = service.create("anna", "Mine");

        BusinessRuleException error = assertThrows(BusinessRuleException.class, () -> service.reassign(mine.getId(), "tom"));

        assertEquals("tom already has 3 open tasks", error.getMessage());
        assertEquals("anna", repository.findById(mine.getId()).orElseThrow().getAssignee());
    }

    @Test
    void reassign_toAPersonWithTwoOpenTasks_isAllowed() {
        service.create("tom", "One");
        service.create("tom", "Two");
        Task mine = service.create("anna", "Mine");

        assertEquals("tom", service.reassign(mine.getId(), "tom").getAssignee());
    }

    @Test
    void reassign_aDoneTaskOfTheNewPerson_doesNotCountForTheLimit() {
        Task one = service.create("tom", "One");
        service.create("tom", "Two");
        service.create("tom", "Three");
        service.complete(one.getId());
        Task mine = service.create("anna", "Mine");

        assertEquals("tom", service.reassign(mine.getId(), "tom").getAssignee());
    }
}
