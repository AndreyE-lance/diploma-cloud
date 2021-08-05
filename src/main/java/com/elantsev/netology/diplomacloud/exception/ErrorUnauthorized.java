package com.elantsev.netology.diplomacloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ErrorUnauthorized extends RuntimeException {
    public ErrorUnauthorized(String message) {
        super(message);
    }
}