package com.elantsev.netology.diplomacloud.exception;

public class ErrorInputData extends RuntimeException {
    public ErrorInputData(String message) {
        super(message);
    }
}