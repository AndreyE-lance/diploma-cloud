package com.elantsev.netology.diplomacloud.exception;

public class ErrorGettingFileList extends RuntimeException {
    public ErrorGettingFileList(String message) {
        super(message);
    }
}