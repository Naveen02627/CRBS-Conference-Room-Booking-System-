package com.TechnoCarts.CRBS.HandleExaption;

public class UserNotAuthorise extends RuntimeException{
    public UserNotAuthorise(String message){
        super(message);
    }
}
