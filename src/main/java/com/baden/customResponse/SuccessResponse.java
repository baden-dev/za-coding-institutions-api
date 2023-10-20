package com.baden.customResponse;

import lombok.Data;

@Data
public class SuccessResponse {

    private int statusCode;
    private String message;

    public SuccessResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}

