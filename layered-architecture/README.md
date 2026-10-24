# Layered Architecture - Step By Step Guide

## The Idea

Think about a restaurant.

- The **waiter** talks to the guests. He takes the order and brings the food. He does not cook.
- The **cook** follows the rules of the kitchen. He does not talk to guests and does not go to the warehouse.
- The **warehouse** keeps the ingredients. It does not know about guests or recipes.

Everybody has **one place** and talks only to the next one: guest → waiter → cook → warehouse. If you want a new menu, you change the kitchen, not the warehouse. If you want a new warehouse, the cook does not notice.

A **layered application** is organized in the same way:

```text
   Presentation  ──►  Service  ──►  Repository         (arrows = "may use")
   (the screen,       (the rules)    (the storage)
    the commands)
        │                │               │
        └────────────────┴───────────────┴──────►  Model  (the data that everybody shares)
```

| Layer | Its only job | Example in this module |
|---|---|---|
| **Presentation** | Talk to the outside world: read a command, write an answer. | `TaskController` |
| **Service** | The **business rules**: what is allowed, what is not. | `TaskService` |
| **Repository** (data) | Keep and load the data. | `InMemoryTaskRepository` |
| **Model** | The data itself. | `Task` |

### The Dependency Rule

> An arrow may only point **down**, to the **next** layer. Never **up**, and never **jump over** a layer.

```text
OK:         Presentation ──► Service ──► Repository
NOT OK:     Presentation ──► Repository       (jumps over the service: the rules are skipped!)
NOT OK:     Repository   ──► Presentation     (points UP: the storage depends on the screen!)
```

Why? Because then **one change touches one layer**: a new screen does not change the rules, a new database does not change the screen, and every layer can be tested alone.

## How Your Work Is Checked

Two kinds of tests:

1. **ArchUnit tests** (`LayerRulesTest`). They do not run your code: they read the compiled classes and check **who uses whom**. When a rule breaks, the message names the class and the line.
2. **Behavior tests.** They check what the program does. In levels 2 and 3 they must stay green while you change the structure: that is what a **refactoring** is.

## The 6 Levels

Each level has its own package: `com.example.layers.level1` ... `level6`. Each class that you must write or fix has a `TODO` and a `TEMPORARY` line.

| Level | Topic | You do |
|---|---|---|
| 1 | Put every class in its layer | **Move** 6 classes into the right packages |
| 2 | The dependency rule | **Fix** two broken arrows |
| 3 | DTOs and mapping | Stop leaking the model, write `TaskMapper` |
| 4 | Business rules and errors | Rules in the **service**, error answers in the **controller** |
| 5 | A new feature through all the layers | Add `reassign` to service and controller |
| 6 | Wrap a layer | Write `AuditingTaskRepository`, change nothing else |

## What You Need

- JDK 21 or newer
- Maven (or the Maven in IntelliJ IDEA)

## Step 1 - Run The Demo

Run `com.example.layers.Main` in IntelliJ.

Or in the terminal, from this folder:

```bash
mvn -q compile
java -cp target/classes com.example.layers.Main
```

You see 6 parts. Each part prints what it must show, for example `(must be ...)`. Right now many answers are wrong. This is the starting point.

## Step 2 - Run The Tests

```bash
mvn test                              # everything
mvn test -Dtest=LayerRulesTest        # only the structure rules (ArchUnit)
mvn test -Dtest=TaskServiceTest       # the service tests of levels 4 and 5
```

---

## Level 1 - Every Class In Its Layer

Open the package `level1.app`. Six classes live in **one** package: it works, but nobody can see the architecture.

Move them (in IntelliJ: select a class, press `F6`, "Move"):

| Class | New package |
|---|---|
| `Task` | `level1.model` |
| `TaskRepository`, `InMemoryTaskRepository` | `level1.repository` |
| `TaskService` | `level1.service` |
| `TaskController` | `level1.presentation` |
| `ConsoleApp` | **`level1`** (the package of the level, **outside** the layers) |

Why is `ConsoleApp` outside the layers? It builds **all** the objects and connects them, so it knows every layer. That is the **composition root** (you met it in `dependency-injection`). The starting point of the program belongs to no layer.

If you move the classes by hand, fix the `package` line and add the `import` lines. The program must still compile and run.

## Level 2 - Fix The Arrows

The layers exist, and the program works. But two arrows are wrong. The tests `level2_...` of `LayerRulesTest` tell you which ones. Both are in the code with the word `TEMPORARY`:

1. **The data layer prints text.** `InMemoryTaskRepository.findAllAsText()` uses `TaskFormatter` (a class of the presentation layer): an arrow that points **up**. Formatting for the screen is not a job of the storage. **Delete** `findAllAsText()` from the interface and from the class.
2. **The controller jumps over the service.** `TaskService.getRepository()` hands the repository to the controller, and the controller calls it directly. **Delete** `getRepository()`, and in the controller use `service.findAll()`, then `TaskFormatter.format(...)`.

When you finish, the output of the program is **exactly the same**. The behavior tests (`TaskControllerTest` of level 2) were green at the start and must be green at the end.

## Level 3 - DTOs: Do Not Leak The Model

Look at `level3.presentation.TaskController`: `list()` and `get()` return the **model** (`Task`). The model has an **internal note**. It goes out to the screen. And the screen is now tied to the structure of the data: if you rename a field in the model, the screen breaks.

A **DTO** (Data Transfer Object) is a small object that shows **only what the outside may see**. `TaskResponse` is the DTO of a task: look at what it does not have.

1. Write `TaskMapper.toResponse` and `toResponses`: they turn the model into DTOs.
2. Change `list()` to return `List<TaskResponse>` and `get(id)` to return `TaskResponse`.

The mapper is the only class of the presentation layer that touches the model.

## Level 4 - Rules And Errors, Each In Its Layer

Two different jobs, and two different layers:

| What | Where | Example |
|---|---|---|
| A **business rule** | the **service** | "a person has at most 3 open tasks" |
| Translating an error into an **answer** | the **controller** | `NotFoundException` → `Error 404: task 99 not found` |

The service does not know what a screen is, so it **throws exceptions** (`NotFoundException`, `BusinessRuleException`). The controller **catches** them and writes a clear answer. The user never sees a stack trace.

**4a. `TaskService`:** write the rules of `create` and `complete` (the comment lists them with the exact messages). Careful with the boundary: the **3rd** open task is allowed, the **4th** is not. A task that is done does not count.

**4b. `TaskController`:** add the two missing `catch` blocks: 404 and 409.

## Level 5 - A New Feature Through All The Layers

The boss asks: "A task must be movable to another person." This is the real test of an architecture: **where do you put the new code?**

- the **rule** ("not if the task is done", "not if the new person has 3 open tasks") → the **service**
- the **command** (`reassign 1 tom`) → the **controller**
- the **repository** → **no change!** `save()` already replaces a task

Write `TaskService.reassign` and the command `reassign` in the controller. Every kind of error already has its answer: your new feature uses the existing `catch` blocks.

## Level 6 - Wrap A Layer

If the layers are good, you can **replace or wrap** one without touching the others. Write `AuditingTaskRepository`: it wraps another repository, **delegates** every call to it, and writes one line to a log (`save 1`, `findAll`, `findById 1`). This is the **Decorator** pattern from `structural-patterns`, used to extend a layer.

The service and the controller do not change by one letter, and they do not even know that the audit exists. The ArchUnit rule `level6_theServiceDoesNotKnowTheAuditingRepository` checks that: the service depends only on the `TaskRepository` **interface**.

---

## Common Mistakes

| Problem | Reason |
|---|---|
| ArchUnit: "Layer 'Service' is empty" | You have not moved the classes yet (level 1), or the package name is wrong. |
| Level 1 does not compile | A moved class needs `import` lines for the classes of the other packages. |
| Level 1: a layer rule fails for `ConsoleApp` | It must be in `level1`, **not** in `level1.presentation`. |
| Level 2: the output changed | You deleted too much. The `list` command must still print the same text. |
| Level 3: the ArchUnit rule still fails | A public method of the controller still has `Task` in its signature (also inside a `List<...>`). |
| Level 4: the 3rd task is refused | The condition must be `>= 3` on the tasks that exist **before** you add. |
| Level 4: the user sees `NoSuchElementException` | The service must throw `NotFoundException`, and the controller must catch it. |
| Level 5: a done task counts for the limit | `countOpen` already ignores done tasks. Use it. |
| Level 6: the log says `save 0` | Log the id of the **saved** task (the result of `delegate.save`), not the one you were given. |

## Rules

Use plain Java only. Do not change the tests, the `Main` class, or the classes that are marked `PROVIDED CODE`.
