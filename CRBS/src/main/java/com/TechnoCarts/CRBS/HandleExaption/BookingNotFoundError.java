package com.TechnoCarts.CRBS.HandleExaption;

public class BookingNotFoundError extends RuntimeException{
    public BookingNotFoundError(String message){
        super(message);
    }
}
