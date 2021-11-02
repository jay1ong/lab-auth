/*
 * Copyright (c) [2019-2021] [NorthLan](lan6995@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.im.auth.po;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author NorthLan
 * @date 2021-05-31
 * @url https://noahlan.com
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Embeddable
public class VerifierEmbed implements Serializable {
    private static final long serialVersionUID = 1545277683779994485L;

    /**
     * 值
     */
    @Column
    private String value;

    /**
     * 是否已验证
     */
    @Column(nullable = false)
    private Boolean verified = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VerifierEmbed that = (VerifierEmbed) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return 1938003409;
    }
}
