package cn.jaylong.auth.repository;

import cn.jaylong.auth.enums.AuthException;
import cn.jaylong.auth.po.QUser;
import cn.jaylong.core.exception.BizException;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import cn.jaylong.auth.po.User;
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
