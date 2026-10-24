package com.example.layers.level6;

import com.example.layers.level6.model.Task;
import com.example.layers.level6.presentation.TaskController;
import com.example.layers.level6.repository.AuditingTaskRepository;
import com.example.layers.level6.repository.InMemoryTaskRepository;
import com.example.layers.level6.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks Task 6 (a data layer that wraps another one). PROVIDED CODE - do not change. */
class AuditingTaskRepositoryTest {

    private InMemoryTaskRepository delegate;
    private List<String> log;
    private AuditingTaskRepository repository;

    @BeforeEach
    void setUp() {
        delegate = new InMemoryTaskRepository();
        log = new ArrayList<>();
        repository = new AuditingTaskRepository(delegate, log);
    }

    @Test
    void save_delegates_returnsTheSavedTask_andLogsItsId() {
        Task saved = repository.save(new Task(0, "Write docs", "anna", false));

        assertEquals(new Task(1, "Write docs", "anna", false), saved);
        assertEquals(1, delegate.findAll().size(), "the task must really be saved in the wrapped repository");
        assertEquals(List.of("save 1"), log);
    }

    @Test
    void save_logsTheIdOfTheSavedTask_notTheZeroOfTheNewTask() {
        repository.save(new Task(0, "A", "anna", false));
        repository.save(new Task(0, "B", "anna", false));

        assertEquals(List.of("save 1", "save 2"), log);
    }

    @Test
    void findAll_returnsTheTasksOfTheWrappedRepository_andLogsIt() {
        delegate.save(new Task(0, "A", "anna", false));
        delegate.save(new Task(0, "B", "tom", false));

        List<Task> all = repository.findAll();

        assertEquals(2, all.size());
        assertEquals("B", all.get(1).getTitle());
        assertEquals(List.of("findAll"), log);
    }

    @Test
    void findById_found_returnsTheTask_andLogsTheId() {
        delegate.save(new Task(0, "A", "anna", false));

        assertEquals("A", repository.findById(1).orElseThrow().getTitle());
        assertEquals(List.of("findById 1"), log);
    }

    @Test
    void findById_notFound_isEmpty_andStillLogs() {
        assertTrue(repository.findById(99).isEmpty());
        assertEquals(List.of("findById 99"), log);
    }

    @Test
    void theWholeApplicationWorksWithTheWrappedRepository_andNothingElseChanges() {
        TaskController controller = new TaskController(new TaskService(repository));

        assertEquals("Created task 1", controller.handle("add anna Write docs"));
        assertEquals("Task 1 done", controller.handle("done 1"));
        assertEquals("1 [x] Write docs (anna)", controller.handle("list"));
    }

    @Test
    void theLogShowsEverythingThatTheServiceAskedTheDataLayer() {
        TaskController controller = new TaskController(new TaskService(repository));

        controller.handle("add anna Write docs");
        controller.handle("done 1");

        // add: the service counts the open tasks (findAll), then saves.   done: it loads the task, then saves.
        assertEquals(List.of("findAll", "save 1", "findById 1", "save 1"), log);
    }
}
