package com.elantsev.netology.diplomacloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ErrorBadCredentials extends RuntimeException {
    public ErrorBadCredentials(String message) {
        super(message);
    }
}