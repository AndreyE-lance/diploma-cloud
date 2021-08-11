package com.elantsev.netology.diplomacloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="files")
public class FileInCloud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String filename;
    private long size;
    private Date loaddate;
    private boolean deleted;
    private Date deleteddate;
}
