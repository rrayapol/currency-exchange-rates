package com.exchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyExchangeException.class) 
    public ResponseEntity <Error> handleException(CurrencyExchangeException ex) {
    	Error er = new Error(ex.getMessage(),"Process Failed");
        return new ResponseEntity<Error>(er, HttpStatus.INTERNAL_SERVER_ERROR);

    }
	
}
