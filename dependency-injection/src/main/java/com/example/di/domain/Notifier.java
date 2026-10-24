package com.example.di.domain;

/**
 * Tells somebody something (an email, an SMS, a message in the console ...).
 * An INTERFACE, so the rest of the program does not depend on HOW the message is sent.
 *
 * PROVIDED CODE - do not change.
 */
public interface Notifier {

    void send(String to, String message);
}
