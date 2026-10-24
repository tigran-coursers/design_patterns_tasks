package com.example.di.impl;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Keeps the tasks in a text FILE, one task per line. The tasks are still there when the program restarts.
 * It must work EXACTLY like InMemoryTaskRepository (read the rules in TaskRepository.java), so the rest
 * of the program can use one or the other without changing. That is what "inject an implementation" means.
 *
 * STUDENT TASK 3a - write the three methods.
 *
 * Tips:
 *   - The file may not exist yet: then there are no tasks. (Files.exists(file))
 *   - Read all the lines:   Files.readAllLines(file)       then   TaskCodec.fromLine(line)  for each line
 *   - Write all the lines:  Files.write(file, lines)       (it creates or replaces the file)
 *   - Files methods throw IOException. Wrap it:  throw new UncheckedIOException(e)
 *   - save(): read all tasks, then
 *        id 0           -> the new id is (the biggest id in the file) + 1, or 1 when the file is empty
 *        an id that exists -> REPLACE that task
 *        an id that does not exist -> add it
 *     then write all the tasks back, and return the saved task.
 *   - Keep the tasks ordered by id.
 */
public class FileTaskRepository implements TaskRepository {

    private final Path file;

    public FileTaskRepository(Path file) {
        this.file = file;
    }

    @Override
    public Task save(Task task) {
        // TODO
        throw new UnsupportedOperationException("TODO: save");   // TEMPORARY - delete this line
    }

    @Override
    public List<Task> findAll() {
        // TODO
        throw new UnsupportedOperationException("TODO: findAll");   // TEMPORARY - delete this line
    }

    @Override
    public Optional<Task> findById(int id) {
        // TODO
        throw new UnsupportedOperationException("TODO: findById");   // TEMPORARY - delete this line
    }
}
