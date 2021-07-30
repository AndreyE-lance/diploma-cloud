package com.elantsev.netology.diplomacloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("auth-token")
    private final String authToken;


}