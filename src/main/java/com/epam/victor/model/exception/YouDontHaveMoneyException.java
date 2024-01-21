package com.epam.victor.model.exception;

public class YouDontHaveMoneyException extends RuntimeException {

    public YouDontHaveMoneyException() {
    }

    public YouDontHaveMoneyException(String message) {
        super(message);
    }
}
