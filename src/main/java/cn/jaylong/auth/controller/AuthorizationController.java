package cn.jaylong.auth.controller;

import cn.jaylong.auth.dto.UserRequest;
import cn.jaylong.auth.enums.AuthException;
import cn.jaylong.auth.payload.LoginResponse;
import cn.jaylong.auth.security.model.UserDetails;
import cn.jaylong.auth.service.UserService;
import cn.jaylong.core.exception.BizException;
import cn.jaylong.auth.repository.UserJpaRepository;
import cn.jaylong.auth.service.TokenService;
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

    @ApiOperation("登录")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody UserRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenService.generateToken(authentication);
            userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            loginResponse.setAccessToken(token);
            loginResponse.setId(userDetails.getId());
            loginResponse.setUsername(userDetails.getUsername());
            loginResponse.setEmail(userDetails.getEmail());
            loginResponse.setRoles(roles);
        } else {
            throw new BizException(AuthException.USER_PASSWORD_ERROR);
        }
        return loginResponse;
    }

}
