package com.baden.customResponse.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse {

    private Date timestamp;
    private int statusCode;
    private String error;
    private String path;

    public ExceptionResponse(Date timestamp, int statusCode, String error, String path) {
        super();
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.error = error;
        this.path = path;
    }

}
