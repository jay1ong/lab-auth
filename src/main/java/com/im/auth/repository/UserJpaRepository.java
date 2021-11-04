package com.im.auth.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.im.auth.enums.AuthException;
import com.im.auth.po.QUser;
import com.im.auth.po.User;
import com.im.core.exception.BizException;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/23
 */
@Repository
public interface UserJpaRepository extends EntityGraphJpaRepository<User, String>, EntityGraphQuerydslPredicateExecutor<User> {

    QUser DSL = QUser.user;

    default User loadUserByUsername(String username) {
        return findOne(DSL.username.eq(username)).orElseThrow(() -> new BizException(AuthException.USER_NOT_EXIST));
    }

    Boolean existsByUsername(String username);

}
