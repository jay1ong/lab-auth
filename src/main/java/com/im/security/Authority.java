package com.im.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/29
 */
@Data
@AllArgsConstructor
public class Authority implements GrantedAuthority {

    private String authority;

}
