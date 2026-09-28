package com.RideShare.BtoC.exception;

// thrown when a request breaks one of our ride rules
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
