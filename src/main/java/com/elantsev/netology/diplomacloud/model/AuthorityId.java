package com.elantsev.netology.diplomacloud.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
public class AuthorityId implements Serializable {
    private String username;
    private String authority;
}
