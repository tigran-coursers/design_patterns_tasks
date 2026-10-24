package com.example.di.impl;

import com.example.di.domain.Notifier;

import java.util.ArrayList;
import java.util.List;

/** Does not send anything. It REMEMBERS the messages, so a test (or a report) can look at them. PROVIDED CODE - do not change. */
public class RecordingNotifier implements Notifier {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void send(String to, String message) {
        messages.add(to + ": " + message);
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}
