package com.im.controller;

import com.im.dto.UserRequest;
import com.im.enums.AuthException;
import com.im.exception.BizException;
import com.im.payload.LoginResponse;
import com.im.repository.UserJpaRepository;
import com.im.security.jwt.JwtService;
import com.im.service.TokenService;
import com.im.service.UserDetails;
import com.im.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserJpaRepository jpaRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final JwtService jwtService;

    //    @ApiOperation("登录")
//    @PostMapping("/login")
//    public HttpResult login(
//            @RequestBody UserRequest request
//    ) {
//        HttpResult httpResult = new HttpResult();
//        httpResult.setCode("200");
//        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
//        if (userDetails != null) {
//            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
//                httpResult.setMessage("登录成功");
//                Authentication authentication =
//                        authenticationManager.authenticate(
//                                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword())
//                        );
//                httpResult.setData(tokenService.generateToken(authentication));
//            } else {
//                httpResult.setCode("11001");
//                httpResult.setMessage("用户名或密码错误");
//            }
//        } else {
//            httpResult.setCode("11001");
//            httpResult.setMessage("指定的用户不存在");
//        }
//        return httpResult;
//    }
//
    @ApiOperation("注册")
    @PostMapping("/register")
    public void register(
            @RequestBody UserRequest request
    ) {
        Boolean usernameExist = jpaRepository.existsByUsername(request.getUsername());
        if (usernameExist) {
            throw new BizException(AuthException.USER_EXIST);
        } else {
            userService.saveUser(request.getUsername(), request.getPassword());
        }
    }

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody UserRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        if (userDetails != null) {
            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = tokenService.generateToken(authentication);
                userDetails = (UserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                loginResponse.setToken(token);
                loginResponse.setId(userDetails.getId());
                loginResponse.setUsername(userDetails.getUsername());
                loginResponse.setEmail(userDetails.getEmail());
                loginResponse.setRoles(roles);
            } else {
                throw new BizException(AuthException.USER_PASSWORD_ERROR);
            }
        } else {
            throw new BizException(AuthException.USER_NOT_EXIST);
        }
        return loginResponse;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
//        if (jpaRepository.existsByUsername(request.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        // Create new user's account
//
//        userService.saveUser(request.getUsername(), request.getPassword());
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }

}
