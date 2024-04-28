package com.xazhao.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @Description Created on 2024/03/22.
 * @Author xaZhao
 */

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code;

    @Getter
    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message, HttpStatus code) {
        this.message = message;
        this.code = code.value();
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }
}
