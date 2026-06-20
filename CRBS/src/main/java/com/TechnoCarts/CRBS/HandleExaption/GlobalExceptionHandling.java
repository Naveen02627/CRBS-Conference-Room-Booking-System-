package com.TechnoCarts.CRBS.HandleExaption;


import com.TechnoCarts.CRBS.Dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Component
public class GlobalExceptionHandling {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> UserNotFoundException(
            UserNotFoundException ex) {

        ExceptionDTO dto = new ExceptionDTO(
                ex.getMessage(),
                "User does not exist in database",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthoriseUserException.class)
    public ResponseEntity<ExceptionDTO> UnAuthoriseUserException(UnAuthoriseUserException ex) {
        ExceptionDTO dto = ExceptionDTO.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .details("This User Is Not Authorised")
                .build();
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ExceptionDTO> EmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        ExceptionDTO dto = ExceptionDTO.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .details("Email is already in Data Base")
                .build();
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HallNotFound.class)
    public ResponseEntity<ExceptionDTO> HallNotFound(HallNotFound ex){
        ExceptionDTO dto = ExceptionDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details("Hall Not Found")
                .build();

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotAuthorise.class)
    public ResponseEntity<ExceptionDTO> UserNotAuthorise(UserNotAuthorise ex){
        ExceptionDTO dto = ExceptionDTO.builder()
                .timestamp(LocalDateTime.now())
                .details("User Not Authorised To do this Action")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BookingRequestError.class)
    public ResponseEntity<ExceptionDTO> BookingRequestError(BookingRequestError ex){
        ExceptionDTO dto = ExceptionDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details("Error In Booking Request")
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
