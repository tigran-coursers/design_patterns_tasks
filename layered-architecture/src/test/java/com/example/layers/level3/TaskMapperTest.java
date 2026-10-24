package com.example.layers.level3;

import com.example.layers.level3.model.Task;
import com.example.layers.level3.presentation.TaskMapper;
import com.example.layers.level3.presentation.TaskResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks Task 3a (the mapper). PROVIDED CODE - do not change. */
class TaskMapperTest {

    @Test
    void toResponse_copiesTheFourVisibleFields() {
        Task task = new Task(7, "Write docs", "anna", true, "secret note");

        assertEquals(new TaskResponse(7, "Write docs", "anna", true), TaskMapper.toResponse(task));
    }

    @Test
    void toResponse_aTaskThatIsNotDone() {
        assertEquals(new TaskResponse(1, "Fix bug", "tom", false),
                TaskMapper.toResponse(new Task(1, "Fix bug", "tom", false, "x")));
    }

    @Test
    void theResponseHasNoField_forTheInternalNote() {
        List<String> names = new ArrayList<>();
        for (RecordComponent component : TaskResponse.class.getRecordComponents()) {
            names.add(component.getName());
        }

        assertEquals(Arrays.asList("id", "title", "assignee", "done"), names);
    }

    @Test
    void toResponses_keepsTheOrder() {
        List<TaskResponse> responses = TaskMapper.toResponses(List.of(
                new Task(1, "A", "anna", false, "n"),
                new Task(2, "B", "tom", true, "n"),
                new Task(3, "C", "anna", false, "n")));

        assertEquals(3, responses.size());
        assertEquals("A", responses.get(0).title());
        assertEquals("B", responses.get(1).title());
        assertEquals("C", responses.get(2).title());
    }

    @Test
    void toResponses_ofAnEmptyList_isAnEmptyList() {
        assertTrue(TaskMapper.toResponses(List.of()).isEmpty());
    }

    @Test
    void toResponses_givesANewList_thatYouCanChange() {
        List<Task> tasks = List.of(new Task(1, "A", "anna", false, "n"));

        List<TaskResponse> first = TaskMapper.toResponses(tasks);
        List<TaskResponse> second = TaskMapper.toResponses(tasks);

        assertNotSame(first, second);
        first.clear();
        assertEquals(1, second.size());
    }
}
