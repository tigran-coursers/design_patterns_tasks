# Dependency Injection - Step By Step Guide

## The Idea

Imagine a cook in a restaurant.

- **Bad:** the cook leaves the kitchen, goes to a farm, buys potatoes, and comes back. Every dish takes hours. And the cook can only cook with potatoes from that one farm.
- **Good:** the cook stands in the kitchen. **Somebody else delivers** the ingredients. The cook only cooks. And if tomorrow the potatoes come from another farm, the cook does not even notice.

In code, the cook is a class, and the ingredients are the **other objects it needs** (its *dependencies*): a repository, an email sender, the clock. **Dependency Injection (DI)** means: the class does **not** create them with `new`. They are **given** to it, usually through the constructor.

```java
// BAD: the class chooses and creates its helper
class TaskService {
    private final Notifier notifier = new ConsoleNotifier();
}

// GOOD: the helper is GIVEN. The class only knows the interface.
class TaskService {
    private final Notifier notifier;
    TaskService(Notifier notifier) { this.notifier = notifier; }
}
```

Why does it matter?

| Without DI | With DI |
|---|---|
| You cannot test the class: it prints and saves for real | In a test you give it a **fake** |
| To use a file instead of memory, you change the class | You give it another object. The class does not change |
| The needs of a class are hidden inside its code | The constructor **shows** everything it needs |

## DI, Dependency Inversion, IoC: Three Words

| Word | What it is |
|---|---|
| **Dependency Inversion** (the D of SOLID) | A **principle**: depend on interfaces, not on concrete classes. (You did it in `solid-principles`.) |
| **Dependency Injection** | A **technique** that makes it work: objects are given from outside. |
| **IoC container** (Spring) | A **tool** that does the giving for you. In level 6 you build one. |

## The Application

A small **task manager**. The same few types are used in all the levels (package `domain`, `impl`):

```text
Task              a task: id, title, assignee, done      (it never changes: "withDone" gives a NEW task)
TaskRepository    interface: save, findAll, findById       <- InMemoryTaskRepository, FileTaskRepository
Notifier          interface: send(to, message)             <- ConsoleNotifier, RecordingNotifier
```

## The 7 Levels

Each level has a **bad example** to read (a class with `Bad` in its name, or a comment that says so), and classes with `TODO` and `TEMPORARY` lines for you. A test class checks each level.

| Level | Topic | You write | Test |
|---|---|---|---|
| 1 | Constructor injection | `level1/TaskService` | `TaskServiceTest` |
| 2 | Why constructor, not setter | `level2/WelcomeService` | `WelcomeServiceTest` |
| 3 | Swap the implementation, inject the clock | `impl/FileTaskRepository`, `level3/DeadlineService` | `FileTaskRepositoryTest`, `DeadlineServiceTest` |
| 4 | The composition root | `level4/wiring/CompositionRoot` | `CompositionRootTest` |
| 5 | Remove the Service Locator | `level5/ReportService` | `ReportServiceTest` |
| 6 | **Build a DI container** (hard) | `level6/Container` | `ContainerTest` |
| 7 | The same, with Spring | `level7/AppConfig`, `level7/ReportPrinter` | `SpringBridgeTest` |

Some tests check the **structure**, not the behavior: `DependencyRulesTest` uses **ArchUnit**. It reads the compiled classes and checks who uses whom, for example "only the composition root may know the implementations". When such a rule breaks, the message names the class that did it.

## What You Need

- JDK 21 or newer
- Maven (or the Maven in IntelliJ IDEA)

## Step 1 - Run The Demo

Run `com.example.di.Main` in IntelliJ.

Or in the terminal, from this folder:

```bash
mvn -q compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.example.di.Main
```

You see 7 parts. Each part prints what it must show, for example `(must be 1)`. Right now many answers are wrong. This is the starting point. Run it again after each level.

## Step 2 - Run The Tests

```bash
mvn test
mvn test -Dtest=TaskServiceTest        # one level
mvn test -Dtest=DependencyRulesTest    # the structure rules
```

---

## Level 1 - Constructor Injection

**Read first:** `BadTaskService`. It creates its repository and its notifier with `new`. It is welded to them, and a test would print to the console.

**Your job:** `TaskService` already has the right methods. Only the **wiring** is wrong. Follow the 4 TODOs:

```java
private final _____ repository;       // the INTERFACE (TaskRepository), not InMemoryTaskRepository
private final _____ notifier;         // the INTERFACE (Notifier)

public TaskService(TaskRepository repository, Notifier notifier) {
    this._____ = repository;
    this._____ = notifier;
}
```

Remove the imports of `impl` at the top: if `TaskService` does not import an implementation, it cannot create one. ArchUnit checks exactly that.

## Level 2 - Why The Constructor?

**Read first:** `BadWelcomeService` (setter injection). After `new BadWelcomeService()` the object exists, but it is **half-built**: the notifier is `null`. The error comes later, far from the mistake. A test even shows it.

Turn `WelcomeService` into the safe version:

1. The field is `final`.
2. The constructor **refuses null** with a clear message: `Objects.requireNonNull(notifier, "notifier is required")`.
3. **Delete** the constructor without arguments and the setter.

Now it is impossible to have a `WelcomeService` without a notifier.

## Level 3 - Swap The Implementation, Inject The Clock

**3a: `FileTaskRepository`.** The tasks must survive a restart, so keep them in a text file. It has to behave **exactly** like `InMemoryTaskRepository`. How do we know it does? Both are tested by the **same contract** (`TaskRepositoryContract`): the same tests run on both. If your file version passes, the rest of the program can use either, with no change. That is the real reason for interfaces.

The TODO has the tips: read all lines, change the list, write all lines back. `TaskCodec` turns a task into a line and back.

**3b: the clock.** Today's date is a dependency too. `LocalDate.now()` inside a class makes it impossible to test ("is it overdue?" changes every day). Inject a `java.time.Clock` and write `LocalDate.now(clock)`. In the test, `Clock.fixed(...)` makes time stand still.

## Level 4 - The Composition Root

If nobody says `new` inside the classes, **somebody** must say it. That place is the **composition root**: one place, near `main`, that knows all the implementations and connects everything.

```text
Application
  |-- TaskService ---- InMemoryTaskRepository
  |        `---------- ConsoleNotifier(out)
  `-- ReportPrinter(the SAME TaskService, out)
```

Write `CompositionRoot.build(out)`. Two details that the tests check:

- The `ConsoleNotifier` must print to `out` (the stream that was given), not to `System.out`.
- The printer and the application must share the **same** `TaskService`. If you create a second one, it has its own empty repository, and the report says `Tasks: 0`. Deciding what is shared is the job of the composition root.

## Level 5 - The Service Locator Trap

**Read first:** `ServiceLocator` and `BadReportService`. A Service Locator is a global box: `ServiceLocator.get(TaskRepository.class)`. It looks easy, but the dependency is **hidden**: the constructor says "I need nothing", and the code crashes later because somebody forgot to register something. Tests influence each other through the global box.

Refactor `ReportService`: add a `repository` field, take it in the constructor, and use it. No line may mention `ServiceLocator` any more.

## Level 6 - Build A DI Container (hard)

You have written the wiring by hand. Spring does it by itself. How? In about 40 lines:

```java
Container container = new Container();
container.register(Notifier.class, RecordingNotifier.class);   // "for a Notifier, build a RecordingNotifier"
container.register(TaskService.class);                           // "TaskService builds itself"
TaskService service = container.get(TaskService.class);          // builds everything it needs, by itself
```

The rules are in the comment of `Container`: build with the single public constructor, build its parameters first (go deep), keep every object as a **singleton**, and report three kinds of error (no binding, wrong constructors, a circle). A **circle** (A needs B, B needs A) can never be built: your container must **detect** it, not loop forever.

Use reflection: `getConstructors()`, `getParameterTypes()`, `newInstance(...)`. Think about **recursion**: to build a class, call `get` for each parameter.

## Level 7 - The Same, With Spring

Spring is your container, grown up. `AppConfig` is the composition root of level 4, written as a **description**: you list the beans (`@Bean` methods), and Spring builds and connects them. Notice that `taskService(TaskRepository repository, Notifier notifier)` **receives** the other beans as parameters. Spring injects them, exactly like your container did.

Then `@ComponentScan` and `@Component` let Spring **find** `ReportPrinter` by itself, and give it a `TaskService`.

---

## Common Mistakes

| Problem | Reason |
|---|---|
| `TaskService` still has `new ...` inside | A field is initialized with `new`. Remove it. The constructor assigns it. |
| ArchUnit: "depends on ..impl.." | An `import` of an implementation is left, or a `new` is still there. |
| Level 2: the null test fails | You forgot the message: it must be exactly `notifier is required`. |
| Level 3: `FileTaskRepository` returns ids `0` | `save` did not give a new id (biggest id + 1). |
| Level 3: the clock test fails | One of the two methods still has `LocalDate.now()` without `clock`. |
| Level 4: the report says `Tasks: 0` | The printer got another `TaskService`. Create it once, and share it. |
| Level 4: some lines are missing in the output | `new ConsoleNotifier()` prints to the console. Give it `out`. |
| Level 6: `StackOverflowError` | You did not stop a circle. Check `resolving` before you build. |
| Level 6: the same type is built twice | Save the new object in `instances` (singleton). |
| Level 6: `resolving` is not empty after an error | The `removeLast()` must be in a `finally` block. |
| Level 7: `NoSuchBeanDefinitionException` | A `@Bean` method is missing, or the return type is not what the test asks. |

## Rules

Use plain Java, and Spring only in level 7. Do not change the tests, the `Bad...` classes or the provided classes.
