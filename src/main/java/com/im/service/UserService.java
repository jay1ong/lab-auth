package com.im.service;

import cn.hutool.core.util.RandomUtil;
import com.im.po.User;
import com.im.po.VerifierEmbed;
import com.im.repository.UserJpaRepository;
import com.im.security.model.UserDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/28
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserJpaRepository jpaRepository;

    private final ApplicationContext context;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetails.build(jpaRepository.loadUserByUsername(username));
    }

    public void saveUser(String username, String password) {
        User user = new User();
        String random = RandomUtil.randomNumbers(32);
        user.setId(random);
        user.setUsername(username);
        user.setPassword(((PasswordEncoder) context.getBean("passwordEncoder")).encode(password));
        user.setZoneId("1");
        VerifierEmbed embed = new VerifierEmbed();
        embed.setValue(random);
        user.setEmail(embed);
        embed.setValue(random);
        user.setPhoneNumber(embed);
        jpaRepository.save(user);
    }


}
