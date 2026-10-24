# Design Patterns Tasks

This project contains separate Java practice modules about **design patterns**.

Each module is an independent Maven module with its own `README.md` guide. Tests check the student's work.

The modules will grow from simple to harder topics:

```text
creational patterns   (singleton, factory, builder, immutable)
structural patterns   (bridge, filter, decorator, facade, proxy)
behavioral patterns   (strategy, observer, command ...)
SOLID principles      (single responsibility, open/closed, liskov, interface segregation, dependency inversion)
architecture          (dependency injection, layered architecture)
```

## Project Structure

```text
design_patterns_tasks/
├── README.md
├── pom.xml
├── .gitignore
├── creational-patterns/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── structural-patterns/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── solid-principles/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── dependency-injection/
│   ├── README.md
│   ├── pom.xml
│   └── src/
└── layered-architecture/
    ├── README.md
    ├── pom.xml
    └── src/
```

## Current Modules

```text
creational-patterns   - Singleton, Factory, Builder, Immutable
structural-patterns   - Bridge, Filter, Decorator, Facade, Proxy
solid-principles      - Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion
dependency-injection  - constructor injection, the composition root, a tiny DI container, Spring (7 levels)
layered-architecture  - presentation / service / repository, the dependency rule, DTOs, errors, ArchUnit (6 levels)
```

## What You Need

- JDK 21 or newer
- Maven (or the Maven in IntelliJ IDEA)

No database is needed. (Only the last level of `dependency-injection` uses Spring, and Maven downloads it.)

## Run A Module

In IntelliJ: open the `design_patterns_tasks` folder as a Maven project, then run the `Main` class of the module.

In the terminal:

```bash
cd creational-patterns        # or structural-patterns, solid-principles, layered-architecture
mvn -q compile
java -cp target/classes com.example.patterns.Main        # creational-patterns
java -cp target/classes com.example.structural.Main      # structural-patterns
java -cp target/classes com.example.solid.Main           # solid-principles
java -cp target/classes com.example.layers.Main          # layered-architecture
```

`dependency-injection` uses Spring in its last level, so it needs Maven to run its demo:

```bash
cd dependency-injection
mvn -q compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.example.di.Main
```

## Run The Tests

```bash
mvn test
```

Run the tests of one module only:

```bash
mvn test -pl solid-principles
```

Run one test class only:

```bash
mvn test -Dtest=SingletonTest
```

## Notes

- Each module has a step-by-step guide in its own `README.md`.
- The modules are separate and do not share code.
