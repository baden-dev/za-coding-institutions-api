package com.baden.customResponse.exception;

import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NullFieldException extends PropertyValueException {

    private final String message;

    public NullFieldException(String message) {
        super(message, null, null);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
