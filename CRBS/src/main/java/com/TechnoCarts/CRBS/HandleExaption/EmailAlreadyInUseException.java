package com.TechnoCarts.CRBS.HandleExaption;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String massage) {
        super(massage);
    }
}
