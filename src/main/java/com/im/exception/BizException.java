package com.im.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -1086970002092608347L;

    public BizException(String msg) {
        super(msg);
    }

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(IException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    String code;
    String message;
}
