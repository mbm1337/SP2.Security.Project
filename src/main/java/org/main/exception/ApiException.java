package org.main.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private int statusCode;
    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
