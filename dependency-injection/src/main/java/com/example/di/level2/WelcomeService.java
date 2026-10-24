package com.example.di.level2;

import com.example.di.domain.Notifier;

/**
 * Sends a welcome message to a new user.
 *
 * STUDENT TASK 2 - turn setter injection into CONSTRUCTOR injection. It has 3 advantages:
 *   1. The object is NEVER half-built: you cannot create it without its notifier.
 *   2. The field can be FINAL: nobody can change the notifier later.
 *   3. The constructor shows, in one place, everything the class needs.
 *
 *   TODO 1: make the field final.
 *   TODO 2: in the constructor, refuse null:  Objects.requireNonNull(notifier, "notifier is required")
 *           and save the notifier.
 *   TODO 3: DELETE the constructor without arguments.
 *   TODO 4: DELETE the setter setNotifier.
 *   (Then the class has exactly ONE constructor, and no setter.)
 */
public class WelcomeService {

    private Notifier notifier;                              // TODO 1: final

    public WelcomeService() {                               // TEMPORARY - delete (TODO 3)
    }

    public WelcomeService(Notifier notifier) {
        // TODO 2: refuse null, and save the notifier
    }

    public void setNotifier(Notifier notifier) {            // TEMPORARY - delete (TODO 4)
        this.notifier = notifier;
    }

    /** Sends the message "Welcome, <user>!" to the user. */
    public void welcome(String user) {
        notifier.send(user, "Welcome, " + user + "!");
    }
}
