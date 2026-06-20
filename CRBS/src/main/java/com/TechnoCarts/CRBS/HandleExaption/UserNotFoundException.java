package com.TechnoCarts.CRBS.HandleExaption;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
