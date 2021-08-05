package com.elantsev.netology.diplomacloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ExceptionEntity {
    private String description;
    private int errorCode;
}