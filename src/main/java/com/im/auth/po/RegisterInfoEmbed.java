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
public class RegisterInfoEmbed implements Serializable {
    private static final long serialVersionUID = -1839544143497064464L;

    /**
     * 注册IP
     * <p>len(255.255.255.255) == 15</p>
     */
    @Column(length = 16)
    private String registerIpv4;

    /**
     * 注册Ipv6表示
     * <p>
     * len(0001:0002:0003:0004:0005:0006:0007:0008) == 39
     * len(0001:0002:0003:0004:0005:ffff:255.255.255.255) == 45
     * </p>
     */
    @Column(length = 46)
    private String registerIpv6;

    /**
     * 注册时的时间
     */
    @Column
    private LocalDateTime registerAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegisterInfoEmbed that = (RegisterInfoEmbed) o;

        if (!Objects.equals(registerIpv4, that.registerIpv4)) return false;
        if (!Objects.equals(registerIpv6, that.registerIpv6)) return false;
        return Objects.equals(registerAt, that.registerAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(registerIpv4);
        result = 31 * result + (Objects.hashCode(registerIpv6));
        result = 31 * result + (Objects.hashCode(registerAt));
        return result;
    }
}
