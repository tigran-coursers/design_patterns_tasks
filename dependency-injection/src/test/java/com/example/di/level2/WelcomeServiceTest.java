package com.example.di.level2;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks Task 2 (constructor injection is safer). PROVIDED CODE - do not change. */
class WelcomeServiceTest {

    @Test
    void theBadVersion_isHalfBuilt_andExplodesLater() {
        BadWelcomeService bad = new BadWelcomeService();    // no error here...

        assertThrows(NullPointerException.class, () -> bad.welcome("anna"));   // ...the error is here, far away
    }

    @Test
    void welcome_sendsTheMessageThroughTheGivenNotifier() {
        List<String> sent = new ArrayList<>();
        WelcomeService service = new WelcomeService((to, message) -> sent.add(to + ": " + message));

        service.welcome("anna");

        assertEquals(List.of("anna: Welcome, anna!"), sent);
    }

    @Test
    void constructor_refusesANullNotifier_withAClearMessage() {
        NullPointerException error = assertThrows(NullPointerException.class, () -> new WelcomeService(null));

        assertEquals("notifier is required", error.getMessage());
    }

    @Test
    void theNotifierField_isFinal() {
        for (Field field : WelcomeService.class.getDeclaredFields()) {
            assertTrue(Modifier.isFinal(field.getModifiers()), "The field '" + field.getName() + "' must be final (TODO 1)");
        }
    }

    @Test
    void thereIsExactlyOneConstructor_theOneWithTheNotifier() {
        assertEquals(1, WelcomeService.class.getConstructors().length, "Delete the constructor without arguments (TODO 3)");
        assertEquals(1, WelcomeService.class.getConstructors()[0].getParameterCount());
    }

    @Test
    void thereIsNoSetter() {
        for (Method method : WelcomeService.class.getDeclaredMethods()) {
            assertFalse(method.getName().startsWith("set"), "Delete the setter '" + method.getName() + "' (TODO 4)");
        }
    }
}
