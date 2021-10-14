package com.im.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/28
 */
@NoRepositoryBean
public interface BaseEntityGraphRepository<T, ID extends Serializable> extends
        EntityGraphJpaRepository<T, ID>, EntityGraphQuerydslPredicateExecutor<T> {
}
