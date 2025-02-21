package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class EntityNotFoundExcpetion extends RuntimeException{
    private String entity;
    private String field;
    private String value;

    public EntityNotFoundExcpetion(String entity, String field, String value) {
        super();
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

}
