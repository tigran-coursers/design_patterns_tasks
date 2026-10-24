package com.example.layers.level3.presentation;

/**
 * What the OUTSIDE world may see of a task: a DTO (Data Transfer Object).
 * Look at what is missing: the internal note of the model. It never leaves the application.
 *
 * PROVIDED CODE - do not change.
 */
public record TaskResponse(int id, String title, String assignee, boolean done) {
}
