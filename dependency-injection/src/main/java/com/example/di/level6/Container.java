package com.example.di.level6;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A TINY DI CONTAINER. This is the heart of Spring, in 40 lines.
 *
 *     Container container = new Container();
 *     container.register(Notifier.class, ConsoleNotifier.class);     // "when somebody needs a Notifier, build a ConsoleNotifier"
 *     container.register(TaskService.class);                         // "TaskService builds itself"
 *     TaskService service = container.get(TaskService.class);       // builds EVERYTHING it needs, by itself
 *
 * The rules:
 *   1. get(type) builds an object with the ONE public constructor of the class that is registered for the type.
 *      Every parameter of the constructor is built (or found) with get(...) too: it goes DEEP, as deep as needed.
 *   2. SINGLETON: every type is built only ONE time. The second get(type) returns the SAME object.
 *      (So two classes that need a Notifier share the same Notifier.)
 *   3. A type that was never registered:
 *          throw new IllegalStateException("No binding for " + type.getSimpleName())
 *   4. A class with zero, or two or more public constructors:
 *          throw new IllegalStateException(implementation.getSimpleName() + " must have exactly one public constructor")
 *   5. A circle (A needs B, B needs A) would never end:
 *          throw new IllegalStateException("Circular dependency: A -> B -> A")     (simple class names, in the order of the path)
 *
 * STUDENT TASK 6 - write register(...) and get(...). This is the hardest task. Take your time.
 *
 * Tools:  implementation.getConstructors()      the PUBLIC constructors
 *         constructor.getParameterTypes()       the types of the parameters
 *         constructor.newInstance(args...)      creates the object (throws ReflectiveOperationException: wrap it
 *                                               in an IllegalStateException("Cannot create " + name, e))
 *         resolving                             the types that are being built RIGHT NOW (the path).
 *                                               Put the type in it before you build the parameters, and take it out at the end (finally).
 */
public class Container {

    /** type -> the class that must be built for it (for a concrete class: the class itself) */
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    /** type -> the object that was already built */
    private final Map<Class<?>, Object> instances = new HashMap<>();

    /** the path of the types that are being built now (used to find a circle) */
    private final Deque<Class<?>> resolving = new ArrayDeque<>();

    /** A concrete class that builds itself. */
    public <T> void register(Class<T> type) {
        // TODO: bindings.put(type, type)
    }

    /** "Whenever somebody needs the abstraction, build the implementation." */
    public <T> void register(Class<T> abstraction, Class<? extends T> implementation) {
        // TODO: bindings.put(abstraction, implementation)
    }

    public <T> T get(Class<T> type) {
        // TODO:
        //  1. if instances has the type -> return it (singleton)
        //  2. find the implementation in bindings. None? -> "No binding for ..."
        //  3. if resolving already contains the type -> "Circular dependency: ..." (write the path, then the type again)
        //  4. the implementation must have exactly one public constructor (rule 4)
        //  5. resolving.addLast(type); then, in try: get every parameter, newInstance(...); finally: resolving.removeLast()
        //  6. remember the new object in instances, and return it

        return null;    // TEMPORARY - delete this line
    }
}
