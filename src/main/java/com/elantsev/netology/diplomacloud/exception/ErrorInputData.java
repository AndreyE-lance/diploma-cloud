package com.elantsev.netology.diplomacloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ErrorInputData extends RuntimeException {
    public ErrorInputData(String message) {
        super(message);
    }
}