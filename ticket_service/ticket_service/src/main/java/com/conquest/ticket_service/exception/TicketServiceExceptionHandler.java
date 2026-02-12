package com.conquest.ticket_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketServiceExceptionHandler{
	@ExceptionHandler(TicketServiceException.class)
    public ResponseEntity<String> handleTicketServiceException(TicketServiceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
