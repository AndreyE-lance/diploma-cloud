package com.elantsev.netology.diplomacloud.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ErrorGettingFileList extends RuntimeException {
    public ErrorGettingFileList(String message) {
        super(message);
    }
}