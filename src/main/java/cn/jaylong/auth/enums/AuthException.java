package cn.jaylong.auth.enums;

import cn.jaylong.core.exception.IException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/15
 */
@Getter
@AllArgsConstructor
public enum AuthException implements IException {

    // 登录异常
    USER_NOT_EXIST("110010001", "指定的用户不存在"),
    USER_PASSWORD_ERROR("110010002", "用户名或密码错误"),

    // 注册异常
    USER_EXIST("110010101", "用户已存在"),

    // token异常
    INVALID_TOKEN("110010201", "token签名不合法"),
    TOKEN_EXPIRED("110010202", "token已过期"),
    TOKEN_PARSING_ERROR("110010203", "token解析异常,鉴权失败"),
    TOKEN_PARSING_USERNAME_ERROR("110010204", "token解析用户名异常,鉴权失败"),
    ;
    private final String code;
    private final String message;

}
