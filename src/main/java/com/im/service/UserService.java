package com.im.service;

import com.im.po.User;
import com.im.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/28
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserJpaRepository jpaRepository;

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return jpaRepository.loadUserByUsername(s);
    }
}
