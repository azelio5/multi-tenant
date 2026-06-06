package com.anvar.saas.exceptions;

public class InvalidRequestException extends BusinessException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
