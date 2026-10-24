package com.example.di.level3;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks Task 3b (the clock is a dependency). PROVIDED CODE - do not change.
 *
 * The test builds a clock that is ALWAYS 2020-06-15. So the answers never change, today or in ten years.
 */
class DeadlineServiceTest {

    private final Clock fixed = Clock.fixed(Instant.parse("2020-06-15T10:00:00Z"), ZoneOffset.UTC);
    private final DeadlineService service = new DeadlineService(fixed);

    @Test
    void aDueDateInThePast_isOverdue() {
        // far in the FUTURE of the real calendar: only the injected clock can say that 2999-01-01 is "in the past"
        Clock farFuture = Clock.fixed(Instant.parse("2999-01-02T10:00:00Z"), ZoneOffset.UTC);

        assertTrue(new DeadlineService(farFuture).isOverdue(LocalDate.of(2999, 1, 1)));
    }

    @Test
    void aDueDateOfToday_isNotOverdue() {
        assertFalse(service.isOverdue(LocalDate.of(2020, 6, 15)), "today is not overdue (is the clock injected?)");
    }

    @Test
    void aDueDateInTheFuture_isNotOverdue() {
        assertFalse(service.isOverdue(LocalDate.of(2020, 6, 16)));
    }

    @Test
    void daysLeft_countsFromTheDayOfTheClock() {
        assertEquals(5, service.daysLeft(LocalDate.of(2020, 6, 20)));
    }

    @Test
    void daysLeft_isNegativeForThePast() {
        assertEquals(-3, service.daysLeft(LocalDate.of(2020, 6, 12)));
    }

    @Test
    void daysLeft_today_isZero() {
        assertEquals(0, service.daysLeft(LocalDate.of(2020, 6, 15)));
    }

    @Test
    void whenTheClockMoves_theAnswerMoves() {
        Clock later = Clock.fixed(Instant.parse("2020-06-21T10:00:00Z"), ZoneOffset.UTC);

        assertTrue(new DeadlineService(later).isOverdue(LocalDate.of(2020, 6, 20)));
        assertFalse(service.isOverdue(LocalDate.of(2020, 6, 20)));
    }
}
