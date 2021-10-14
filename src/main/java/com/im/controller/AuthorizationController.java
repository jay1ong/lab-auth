package com.im.controller;

import com.im.HttpResult;
import com.im.dto.UserRequest;
import com.im.po.User;
import com.im.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/30
 */
@RestController
@AllArgsConstructor
@RequestMapping
public class AuthorizationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @ApiOperation("测试登录")
    @PostMapping("/api/login")
    public HttpResult value(
            @RequestBody UserRequest request
    ) {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode("200");
        User user;
        user = userService.loadUserByUsername(request.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                httpResult.setMessage("登录成功");
            } else {
                httpResult.setMessage("用户名或密码错误");
            }
        } else {
            httpResult.setMessage("指定的用户不存在");
        }
        return httpResult;
    }

}
