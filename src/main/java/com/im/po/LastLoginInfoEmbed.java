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

package com.im.po;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
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
public class LastLoginInfoEmbed implements Serializable {
    private static final long serialVersionUID = -9072050662919543142L;

    /**
     * 上次登录时使用的浏览器(UA信息)
     * <p>user-agent</p>
     */
    @Column(length = 64)
    private String ua;

    /**
     * 登录设备
     */
    @Column(length = 64)
    private String device;

    /**
     * 最近一次登录IP
     */
    @Column(length = 16)
    private String lastLoginIp;

    /**
     * 最近一次登录时间
     */
    @Column
    private LocalDateTime lastLoginAt;

    /**
     * 登录次数
     */
    @Column(length = 64)
    private Long loginCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LastLoginInfoEmbed that = (LastLoginInfoEmbed) o;

        if (!Objects.equals(ua, that.ua)) return false;
        if (!Objects.equals(device, that.device)) return false;
        if (!Objects.equals(lastLoginIp, that.lastLoginIp)) return false;
        if (!Objects.equals(lastLoginAt, that.lastLoginAt)) return false;
        return Objects.equals(loginCount, that.loginCount);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(ua);
        result = 31 * result + (Objects.hashCode(device));
        result = 31 * result + (Objects.hashCode(lastLoginIp));
        result = 31 * result + (Objects.hashCode(lastLoginAt));
        result = 31 * result + (Objects.hashCode(loginCount));
        return result;
    }
}
