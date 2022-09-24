package com.edu.ulab.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name="person")
public class PersonEntity extends BaseEntity {
    @Column(name = "full_name")
    private String fullName;
    private String title;
    private int age;
}
