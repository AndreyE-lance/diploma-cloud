package com.elantsev.netology.diplomacloud.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
}
