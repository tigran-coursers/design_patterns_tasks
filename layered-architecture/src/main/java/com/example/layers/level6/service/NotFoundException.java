package com.example.layers.level6.service;

/** A thing that was asked for does not exist. Thrown by the SERVICE layer. PROVIDED CODE - do not change. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
