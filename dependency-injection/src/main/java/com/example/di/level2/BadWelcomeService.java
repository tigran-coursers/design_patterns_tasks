package com.example.di.level2;

import com.example.di.domain.Notifier;

/**
 * THE BAD EXAMPLE - read it, do not change it. This is "setter injection" (and a public field).
 *
 * Look at the problem: after "new BadWelcomeService()", the object EXISTS, but it is half-built.
 * Its notifier is null. It looks ready, and it explodes later, when somebody calls welcome():
 *
 *     BadWelcomeService service = new BadWelcomeService();
 *     service.welcome("anna");        // NullPointerException, far from the real mistake
 *
 * Also: the notifier can be CHANGED at any time by anybody (setNotifier), even while the service is working.
 */
public class BadWelcomeService {

    public Notifier notifier;

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public void welcome(String user) {
        notifier.send(user, "Welcome, " + user + "!");
    }
}
