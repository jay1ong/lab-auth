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

package cn.jaylong.auth.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NorthLan
 * @date 2021-05-31
 * @url https://noahlan.com
 */
@Data
public class AddressData implements Serializable {
    private static final long serialVersionUID = -4967915510876881347L;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份/地区
     */
    private String province;

    /**
     * 市/州
     */
    private String city;

    /**
     * 市辖区/县
     */
    private String district;

    /**
     * 街道地址
     * <p>乡/镇 楼栋号 门牌号 等</p>
     */
    private String streetAddress;

    /**
     * 邮编号码
     */
    private String postalCode;

    /**
     * State, province, prefecture, or region component.
     */
    public String getRegion() {
        return province + city + district;
    }

    /**
     * City or locality component.
     */
    public String getLocality() {
        return city;
    }

    /**
     * Full mailing address, formatted for display or use on a mailing label.
     * This field MAY contain multiple lines, separated by newlines.
     * Newlines can be represented either as a carriage return/line feed pair ("\r\n") or as a single line feed character ("\n").
     */
    public String getFormatted() {
        return country + province + city + streetAddress;
    }
}
