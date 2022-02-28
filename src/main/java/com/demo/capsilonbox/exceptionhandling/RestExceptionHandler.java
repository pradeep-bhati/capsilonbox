package com.demo.capsilonbox.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        Error error = new Error(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }
    
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalServerError(
    		Exception ex) {
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("spmething went wrong");
        return new ResponseEntity<>(error, error.getStatus());
    }
    

}
