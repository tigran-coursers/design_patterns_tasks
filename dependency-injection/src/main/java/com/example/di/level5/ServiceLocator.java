package com.example.di.level5;

import java.util.HashMap;
import java.util.Map;

/**
 * THE BAD IDEA - read it, do not change it. A "Service Locator": a global box where code goes to fetch what it needs.
 *
 *     TaskRepository repository = ServiceLocator.get(TaskRepository.class);     // from anywhere!
 *
 * It looks practical. But:
 *   - the dependency is HIDDEN: the constructor says "I need nothing", and then it crashes at run time
 *   - it is a global variable: tests influence each other
 *   - you can never see what a class needs without reading all its code
 * With injection, the same class says it in its constructor:  new ReportService(repository).
 */
public final class ServiceLocator {

    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    private ServiceLocator() {
    }

    public static <T> void register(Class<T> type, T service) {
        SERVICES.put(type, service);
    }

    public static <T> T get(Class<T> type) {
        Object service = SERVICES.get(type);
        if (service == null) {
            throw new IllegalStateException("No service registered: " + type.getSimpleName());
        }
        return type.cast(service);
    }

    public static void clear() {
        SERVICES.clear();
    }
}
