package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class HttpConnectionException extends RuntimeException {

    String webService;

    public HttpConnectionException(String webService) {
        super();
        this.webService = webService;
    }
}
