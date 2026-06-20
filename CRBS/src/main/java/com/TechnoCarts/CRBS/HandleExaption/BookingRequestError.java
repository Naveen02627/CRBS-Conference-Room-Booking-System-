package com.TechnoCarts.CRBS.HandleExaption;

public class BookingRequestError extends RuntimeException{
    public BookingRequestError(String message){
        super(message);
    }
}
