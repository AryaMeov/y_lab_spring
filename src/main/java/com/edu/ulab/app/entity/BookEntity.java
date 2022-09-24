package com.edu.ulab.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name="book")
public class BookEntity extends BaseEntity {
    private String title;
    private String author;
    @Column(name = "page_count")
    private long pageCount;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private PersonEntity person;
}
