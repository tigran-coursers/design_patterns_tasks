package com.example.di.level5;

import com.example.di.domain.Task;
import com.example.di.domain.TaskRepository;

/**
 * THE BAD EXAMPLE - read it, do not change it.
 * "new BadReportService()" looks innocent. Its constructor needs nothing. And then it crashes,
 * because somebody forgot to register the repository in the ServiceLocator first.
 */
public class BadReportService {

    public long countOpen() {
        TaskRepository repository = ServiceLocator.get(TaskRepository.class);
        long open = 0;
        for (Task task : repository.findAll()) {
            if (!task.isDone()) {
                open++;
            }
        }
        return open;
    }
}
