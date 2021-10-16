package com.im.payload;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/30
 */
@Data
public class HttpResult {

    String code;

    String message;

    Object data;

}
