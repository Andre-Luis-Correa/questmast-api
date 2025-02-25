package com.questmast.questmast.common.exception.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDescription {
    private int statusCode;
    private String message;
}
