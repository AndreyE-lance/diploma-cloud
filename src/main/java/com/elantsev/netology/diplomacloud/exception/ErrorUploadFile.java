package com.elantsev.netology.diplomacloud.exception;

public class ErrorUploadFile extends RuntimeException {
    public ErrorUploadFile(String message) {
        super(message);
    }
}