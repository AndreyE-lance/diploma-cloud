package com.elantsev.netology.diplomacloud.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name="authorities",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "authority"})})
@Embeddable
public class Authority{
    @EmbeddedId
    private AuthorityId id;
}
