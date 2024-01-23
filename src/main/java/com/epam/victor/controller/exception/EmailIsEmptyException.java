package com.epam.victor.controller.exception;

public class EmailIsEmptyException extends RuntimeException{
    public EmailIsEmptyException() {
    }

    public EmailIsEmptyException(String message) {
        super(message);
    }
}
