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

package com.im.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author NorthLan
 * @date 2021-02-05
 * @url https://noahlan.com
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusEnum {
    NORMAL("normal", "正常"),
    DISABLED("disabled", "禁用"),
    LOCK("lock", "锁定"),
    EXPIRED("expired", "过期"),
    ;
    private final String code;
    private final String message;

    public static StatusEnum getStatusByCode(String code) {
        return Arrays.stream(values()).filter(statusEnum -> StrUtil.equals(statusEnum.getCode(), code)).findFirst().orElse(null);
    }

}
