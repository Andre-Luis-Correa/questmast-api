package com.questmast.questmast;

import jakarta.persistence.Id;


public class Pessoa {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
