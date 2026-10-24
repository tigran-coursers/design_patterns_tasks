package com.example.di.level7;

import org.springframework.context.annotation.Configuration;

/**
 * THE COMPOSITION ROOT, IN SPRING. Compare it with level 4: it is the same idea. Instead of writing
 * "new" and passing the objects by hand, you DESCRIBE the beans, and Spring does the wiring
 * (and keeps every bean as a singleton, like your Container in level 6).
 *
 * STUDENT TASK 7a - describe three beans. A bean is a method with the annotation @Bean. Its RETURN TYPE is the
 * type of the bean, and its PARAMETERS are given by Spring (it finds the other beans by their types):
 *
 *     @Bean
 *     public TaskRepository taskRepository() {
 *         return new InMemoryTaskRepository();
 *     }
 *
 *     @Bean
 *     public Notifier notifier() {
 *         return new RecordingNotifier();
 *     }
 *
 *     @Bean
 *     public TaskService taskService(TaskRepository repository, Notifier notifier) {   // Spring gives the two beans
 *         return new TaskService(repository, notifier);                                // do NOT write "new" for them here!
 *     }
 *
 * STUDENT TASK 7b - find the components. Add this annotation above the class:
 *
 *     @ComponentScan(basePackageClasses = ReportPrinter.class)
 *
 * (and do not forget the @Component on ReportPrinter).
 *
 * Imports: org.springframework.context.annotation.Bean, ComponentScan; com.example.di.domain.*; com.example.di.impl.*
 */
@Configuration
public class AppConfig {

    // TODO 7a: the three @Bean methods
}
