package com.example.di.level7;

import com.example.di.domain.Notifier;
import com.example.di.domain.TaskRepository;
import com.example.di.impl.InMemoryTaskRepository;
import com.example.di.impl.RecordingNotifier;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Checks Task 7 (the same thing, with Spring). PROVIDED CODE - do not change.
 *
 * "new AnnotationConfigApplicationContext(AppConfig.class)" starts a real Spring container: your Container of level 6,
 * grown up. getBean(...) is your get(...).
 */
class SpringBridgeTest {

    @Test
    void theRepositoryBean_isAnInMemoryRepository() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertInstanceOf(InMemoryTaskRepository.class, context.getBean(TaskRepository.class));
        }
    }

    @Test
    void theNotifierBean_isARecordingNotifier() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertInstanceOf(RecordingNotifier.class, context.getBean(Notifier.class));
        }
    }

    @Test
    void theTaskServiceBean_isBuiltWithTheOtherBeans_notWithNewObjects() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TaskService service = context.getBean(TaskService.class);

            service.create("Learn Spring", "anna");

            // the task is in the repository BEAN, and the message is in the notifier BEAN: they are shared
            assertEquals(1, context.getBean(TaskRepository.class).findAll().size());
            assertEquals(List.of("anna: New task: Learn Spring"), ((RecordingNotifier) context.getBean(Notifier.class)).getMessages());
        }
    }

    @Test
    void everyBeanIsASingleton_likeInYourContainer() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertSame(context.getBean(TaskService.class), context.getBean(TaskService.class));
            assertSame(context.getBean(TaskRepository.class), context.getBean(TaskRepository.class));
        }
    }

    @Test
    void theReportPrinter_isFoundByComponentScan_andGetsTheSameTaskService() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ReportPrinter printer = context.getBean(ReportPrinter.class);

            assertSame(context.getBean(TaskService.class), printer.getService());
        }
    }

    @Test
    void theReportPrinter_seesTheTasksOfTheService() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(TaskService.class).create("A", "anna");
            context.getBean(TaskService.class).create("B", "anna");

            assertEquals("Tasks: 2", context.getBean(ReportPrinter.class).report());
        }
    }
}
