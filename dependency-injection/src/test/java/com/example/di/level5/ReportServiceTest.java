package com.example.di.level5;

import com.example.di.domain.Task;
import com.example.di.impl.InMemoryTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Checks Task 5 (no service locator). PROVIDED CODE - do not change. */
class ReportServiceTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    @AfterEach
    void emptyTheLocator() {
        ServiceLocator.clear();   // the locator is a GLOBAL variable: every test must clean it. Another reason not to use it.
        repository = new InMemoryTaskRepository();
    }

    @Test
    void theBadVersion_crashesWhenNobodyRegisteredTheRepository() {
        BadReportService bad = new BadReportService();   // looks fine...

        assertThrows(IllegalStateException.class, bad::countOpen);   // ...and fails here
    }

    @Test
    void countOpen_countsTheTasksThatAreNotDone() {
        repository.save(new Task(0, "A", "anna", false));
        repository.save(new Task(0, "B", "anna", true));
        repository.save(new Task(0, "C", "tom", false));

        assertEquals(2, new ReportService(repository).countOpen());
    }

    @Test
    void countOpen_noTasks_isZero() {
        assertEquals(0, new ReportService(repository).countOpen());
    }

    @Test
    void titlesFor_returnsOnlyTheTasksOfThatPerson_inOrder() {
        repository.save(new Task(0, "A", "anna", false));
        repository.save(new Task(0, "B", "tom", false));
        repository.save(new Task(0, "C", "anna", true));

        assertEquals(List.of("A", "C"), new ReportService(repository).titlesFor("anna"));
    }

    @Test
    void titlesFor_unknownPerson_isEmpty() {
        repository.save(new Task(0, "A", "anna", false));

        assertEquals(List.of(), new ReportService(repository).titlesFor("nobody"));
    }

    @Test
    void theServiceUsesTheRepositoryThatWasGiven_notTheOneInTheLocator() {
        InMemoryTaskRepository fromTheLocator = new InMemoryTaskRepository();
        fromTheLocator.save(new Task(0, "From the locator", "anna", false));
        ServiceLocator.register(com.example.di.domain.TaskRepository.class, fromTheLocator);

        assertEquals(0, new ReportService(repository).countOpen(), "it must look only at the repository of the constructor");
    }
}
