package com.im.controller;

import com.im.po.User;
import com.im.repository.UserJpaRepository;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserJpaRepository repository;


    @ApiOperation("根据用户名获取用户信息")
    @GetMapping("/username")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') ")
    public User getUserByUsername(@RequestParam String username) {
        return repository.loadUserByUsername(username);
    }

}
