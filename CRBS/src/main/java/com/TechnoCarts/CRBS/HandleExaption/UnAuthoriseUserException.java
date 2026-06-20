package com.TechnoCarts.CRBS.HandleExaption;

public class UnAuthoriseUserException extends RuntimeException{

    public UnAuthoriseUserException(String massage){
        super(massage);
    }
}
