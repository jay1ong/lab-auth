package com.im.auth.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiMessage {

    private String code;
    private LocalDateTime timestamp;
    private String message;
    private Object data;
    private String description;

}
