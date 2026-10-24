package com.example.di.level4;

import com.example.di.level4.wiring.CompositionRoot;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/** Checks Task 4 (the composition root). PROVIDED CODE - do not change. */
class CompositionRootTest {

    private String run(String... titles) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);

        Application application = CompositionRoot.build(out);
        assertNotNull(application, "build() must return the Application");
        application.run(titles);

        return bytes.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    void theApplicationWorksEndToEnd() {
        String output = run("Write docs", "Fix bug");

        assertEquals("""
                [notify] team: New task: Write docs
                [notify] team: New task: Fix bug
                Tasks: 2
                  - [ ] Write docs (team)
                  - [ ] Fix bug (team)
                """, output);
    }

    @Test
    void theNotifierPrintsToTheStreamThatWasGiven_notToSystemOut() {
        String output = run("One");

        assertEquals(true, output.contains("[notify] team: New task: One"),
                "the ConsoleNotifier must print to 'out' (new ConsoleNotifier(out))");
    }

    @Test
    void theServiceAndThePrinterShareTheSameRepository() {
        String output = run("One", "Two", "Three");

        assertEquals(true, output.contains("Tasks: 3"),
                "the report said something else: the printer got its OWN service. Give it the SAME one");
    }

    @Test
    void everyBuildMakesANewApplication_withoutSharedState() {
        String first = run("A");
        String second = run("A");

        assertEquals(first, second, "the second application must start empty (no static field, no shared repository)");
    }

    @Test
    void buildGivesADifferentObjectEveryTime() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());

        assertNotSame(CompositionRoot.build(out), CompositionRoot.build(out));
    }
}
