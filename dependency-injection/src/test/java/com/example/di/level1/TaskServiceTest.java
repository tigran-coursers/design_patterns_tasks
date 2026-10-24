package com.example.di.level1;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;
import com.example.di.impl.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks Task 1 (constructor injection). PROVIDED CODE - do not change.
 *
 * Look at what the test can do now: it GIVES the service a fake notifier that remembers the messages,
 * and a repository that counts the saves. With the bad version this was impossible.
 */
class TaskServiceTest {

    /** A repository that counts how many times it was asked to save. */
    static class CountingRepository extends InMemoryTaskRepository {
        int saves;

        @Override
        public Task save(Task task) {
            saves++;
            return super.save(task);
        }
    }

    private CountingRepository repository;
    private List<String> sent;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new CountingRepository();
        sent = new ArrayList<>();
        service = new TaskService(repository, (to, message) -> sent.add(to + ": " + message));
    }

    @Test
    void create_savesTheTaskInTheRepositoryThatWasGiven() {
        Task task = service.create("Write docs", "anna");

        assertEquals(1, repository.saves, "the service must use the repository that it was given");
        assertEquals(new Task(1, "Write docs", "anna", false), task);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void create_tellsTheAssigneeThroughTheNotifierThatWasGiven() {
        service.create("Write docs", "anna");

        assertEquals(List.of("anna: New task: Write docs"), sent);
    }

    @Test
    void complete_marksTheTaskDone_andTellsTheAssignee() {
        Task created = service.create("Write docs", "anna");
        sent.clear();

        Task done = service.complete(created.getId());

        assertTrue(done.isDone());
        assertTrue(repository.findById(created.getId()).orElseThrow().isDone());
        assertEquals(List.of("anna: Task done: Write docs"), sent);
    }

    @Test
    void complete_unknownTask_throws() {
        NoSuchElementException error = assertThrows(NoSuchElementException.class, () -> service.complete(99));

        assertEquals("Task not found: 99", error.getMessage());
    }

    @Test
    void findAll_showsWhatIsInTheGivenRepository() {
        repository.save(new Task(0, "Old task", "tom", false));

        assertEquals(1, service.findAll().size());
        assertEquals("Old task", service.findAll().get(0).getTitle());
    }

    @Test
    void theFieldsAreInterfaces_notConcreteClasses() {
        for (Field field : TaskService.class.getDeclaredFields()) {
            assertTrue(field.getType().isInterface(),
                    "The field '" + field.getName() + "' has the type " + field.getType().getSimpleName()
                            + ". Use the interface (TODO 1)");
        }
    }

    @Test
    void theFieldsAreFinal() {
        for (Field field : TaskService.class.getDeclaredFields()) {
            assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()), "The field '" + field.getName() + "' must stay final");
        }
    }

    @Test
    void theBadVersion_stillWorks_butYouCannotGiveItAnything() {
        // For comparison: it has no constructor that accepts helpers. It chooses its own.
        assertEquals(0, BadTaskService.class.getConstructors()[0].getParameterCount());
    }

    @Test
    void sanityCheck_aServiceNeedsARepository() {
        TaskRepository other = new InMemoryTaskRepository();
        TaskService another = new TaskService(other, (to, message) -> { });

        another.create("X", "anna");

        assertEquals(1, other.findAll().size());
        assertEquals(0, repository.findAll().size(), "two services must not share a repository by accident");
    }
}
