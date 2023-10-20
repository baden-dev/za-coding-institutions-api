package com.baden.customResponse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handlerNoSuchElementException(NoSuchElementException e, WebRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), 404, e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullFieldException.class)
    public @ResponseBody ResponseEntity<ExceptionResponse> handlerCustomException(NullFieldException e, WebRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), 400, e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
