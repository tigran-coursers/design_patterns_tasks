package com.example.layers.level3.presentation;

import com.example.layers.level3.model.Task;
import com.example.layers.level3.service.TaskService;

import java.util.List;

/**
 * The PRESENTATION layer. Its public methods are what the outside world sees (a screen, a web client).
 *
 * STUDENT TASK 3b - STOP LEAKING THE MODEL.
 * Now list() and get() hand out the model objects (Task). Those carry the internal note, and the screen
 * becomes tied to the structure of the database. Give out DTOs instead:
 *
 *   TODO 1: list() must return  List<TaskResponse>  (use TaskMapper.toResponses)
 *   TODO 2: get(id) must return  TaskResponse        (use TaskMapper.toResponse)
 *
 * ArchUnit checks that no public method of the presentation layer shows a model class.
 */
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    public List<Task> list() {                      // TEMPORARY - TODO 1
        return service.findAll();
    }

    public Task get(int id) {                       // TEMPORARY - TODO 2
        return service.findById(id);
    }
}
