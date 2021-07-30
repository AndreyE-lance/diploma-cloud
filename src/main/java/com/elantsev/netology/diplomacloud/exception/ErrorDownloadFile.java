package com.elantsev.netology.diplomacloud.exception;

public class ErrorDownloadFile extends RuntimeException {
    public ErrorDownloadFile(String message) {
        super(message);
    }
}