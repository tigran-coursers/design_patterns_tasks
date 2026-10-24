package com.example.di.level3;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Answers questions about deadlines: "is it late?", "how many days are left?".
 *
 * The answer depends on TODAY. If the class asks the system clock by itself (LocalDate.now()), you can never
 * test it: the result changes every day, and a test for "overdue" would break tomorrow.
 * The CURRENT TIME is a dependency too. Inject it: a java.time.Clock. In production it is the real clock,
 * in a test it is a clock that always shows the same day:   Clock.fixed(...)
 *
 * STUDENT TASK 3b - use the injected clock.
 *
 *   TODO: in both methods, replace  LocalDate.now()  by  LocalDate.now(clock)
 *         (the field "clock" is already saved by the constructor).
 */
public class DeadlineService {

    private final Clock clock;

    public DeadlineService(Clock clock) {
        this.clock = clock;
    }

    /** True when the due date is BEFORE today. The due date itself is NOT overdue. */
    public boolean isOverdue(LocalDate dueDate) {
        return dueDate.isBefore(LocalDate.now());                      // TEMPORARY - use the clock
    }

    /** The number of days from today to the due date. Negative when the due date is in the past. */
    public long daysLeft(LocalDate dueDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);      // TEMPORARY - use the clock
    }
}
