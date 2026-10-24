package com.example.layers.level5.service;

/** A business rule says no. Thrown by the SERVICE layer. PROVIDED CODE - do not change. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
