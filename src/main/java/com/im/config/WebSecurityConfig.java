package com.im.config;

import com.im.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/28
 */
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-ui",
                "/swagger-resources",
                "/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/security",
                "/api/login"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/favicon.ico").permitAll()
                .antMatchers("/swagger-ui", "/swagger-ui/**", "/test/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
                .and()
//                .httpBasic()
//                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                .userDetailsService(userService)
                .key("lab")
        ;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
