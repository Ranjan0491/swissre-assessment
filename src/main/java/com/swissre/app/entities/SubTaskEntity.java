package com.swissre.app.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class SubTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private boolean completed;
}
