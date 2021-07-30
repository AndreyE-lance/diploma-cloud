package com.elantsev.netology.diplomacloud.exception;

public class ErrorUnauthorized extends RuntimeException {
    public ErrorUnauthorized(String message) {
        super(message);
    }
}