package com.example.di.impl;

import com.example.di.domain.Notifier;

import java.io.PrintStream;

/** "Sends" a message by printing it: "[notify] anna: New task: Write docs". PROVIDED CODE - do not change. */
public class ConsoleNotifier implements Notifier {

    private final PrintStream out;

    public ConsoleNotifier() {
        this(System.out);
    }

    public ConsoleNotifier(PrintStream out) {
        this.out = out;
    }

    @Override
    public void send(String to, String message) {
        out.println("[notify] " + to + ": " + message);
    }
}
