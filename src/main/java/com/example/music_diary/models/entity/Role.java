package com.example.music_diary.models.entity;

import com.example.music_diary.models.enums.Roles;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    public Role() {

    }

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    public Roles getName() {
        return name;
    }

    public void setName(Roles name) {
        this.name = name;
    }
}
