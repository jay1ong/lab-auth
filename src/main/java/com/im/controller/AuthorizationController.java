package com.im.controller;

import com.im.HttpResult;
import com.im.dto.UserRequest;
import com.im.po.User;
import com.im.repository.UserJpaRepository;
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
@RequestMapping("auth")
public class AuthorizationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserJpaRepository jpaRepository;

    @ApiOperation("登录")
    @PostMapping("/login")
    public HttpResult login(
            @RequestBody UserRequest request
    ) {
        HttpResult httpResult = new HttpResult();
        User user;
        httpResult.setCode("200");
        user = userService.loadUserByUsername(request.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                httpResult.setMessage("登录成功");
            } else {
                httpResult.setCode("11001");
                httpResult.setMessage("用户名或密码错误");
            }
        } else {
            httpResult.setCode("11001");
            httpResult.setMessage("指定的用户不存在");
        }
        return httpResult;
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public HttpResult register(
            @RequestBody UserRequest request
    ) {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode("200");
        Boolean usernameExist = jpaRepository.existsByUsername(request.getUsername());
        if (usernameExist) {
            httpResult.setCode("11002");
            httpResult.setMessage("用户已存在");
        } else {
            userService.saveUser(request.getUsername(), request.getPassword());
        }
        return httpResult;
    }

}
