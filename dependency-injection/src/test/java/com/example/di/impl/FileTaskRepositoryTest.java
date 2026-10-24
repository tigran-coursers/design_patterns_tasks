package com.example.di.impl;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks Task 3a. It passes the SAME contract as the in-memory one, and a few things that are special for a file. PROVIDED CODE - do not change. */
class FileTaskRepositoryTest extends TaskRepositoryContract {

    @TempDir
    Path directory;

    private Path file;

    @Override
    protected TaskRepository createRepository() {
        file = directory.resolve("tasks.txt");
        return new FileTaskRepository(file);
    }

    @Test
    void theFileDoesNotExistAtTheStart_andThatIsNotAnError() {
        assertFalse(Files.exists(file));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void theTasksSurviveARestart_aNewRepositoryOnTheSameFileSeesThem() {
        repository.save(new Task(0, "Write docs", "anna", false));
        repository.save(new Task(0, "Fix bug", "tom", true));

        TaskRepository afterRestart = new FileTaskRepository(file);

        assertEquals(2, afterRestart.findAll().size());
        assertEquals("Fix bug", afterRestart.findById(2).orElseThrow().getTitle());
    }

    @Test
    void theNewIdContinuesAfterARestart() {
        repository.save(new Task(0, "A", "anna", false));
        repository.save(new Task(0, "B", "anna", false));

        TaskRepository afterRestart = new FileTaskRepository(file);

        assertEquals(3, afterRestart.save(new Task(0, "C", "anna", false)).getId());
    }

    @Test
    void theFileHasOneLinePerTask() throws Exception {
        repository.save(new Task(0, "Write docs", "anna", false));
        repository.save(new Task(0, "Fix bug", "tom", true));

        assertEquals(java.util.List.of("1;Write docs;anna;false", "2;Fix bug;tom;true"), Files.readAllLines(file));
    }
}
