package com.elantsev.netology.diplomacloud.exception;

public class ErrorBadCredentials extends RuntimeException {
    public ErrorBadCredentials(String message) {
        super(message);
    }
}