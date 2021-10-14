package com.im.repository;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.im.po.QUser;
import com.im.po.User;
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
        return findOne(DSL.username.eq(username)).orElseThrow(() -> ExceptionUtil.wrapRuntime("指定的用户不存在"));
    }

}
