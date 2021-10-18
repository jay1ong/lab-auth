package com.im.security.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/16
 */
@Data
@Builder
public class Authorization {

    String issuer;

    Date issueTime;

    Date expirationTime;

    Date notBeforeTime;

    String subject;

    Object claim;

    String audience;

    List<String> audienceList;

    String jwtID;

}
