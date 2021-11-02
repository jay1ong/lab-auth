package com.im.auth.service;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.auth.enums.AuthException;
import com.im.auth.exception.BizException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService implements AuthenticationEntryPoint {

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("self")
                .issueTime(new Date(now.toEpochMilli()))
                .expirationTime(new Date(now.plusSeconds(expiry).toEpochMilli()))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        // @formatter:on
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT).build();
        SignedJWT jwt = new SignedJWT(header, claims);
        return sign(jwt).serialize();
    }

    SignedJWT sign(SignedJWT jwt) {
        try {
            jwt.sign(new RSASSASigner(this.privateKey));
            return jwt;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public boolean validateToken(String authToken) throws JOSEException {
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(authToken);
        } catch (ParseException e) {
            throw new BizException(AuthException.TOKEN_PARSING_ERROR);
        }
        JWSVerifier jwsVerifier = new RSASSAVerifier(this.publicKey);
        if (!jwsObject.verify(jwsVerifier)) {
            throw new BizException(AuthException.INVALID_TOKEN);
        }
        Map<String, Object> map = jwsObject.getPayload().toJSONObject();

        long exp = new Date(Convert.toLong(map.get("exp")) * 1000).getTime();
        long now = new Date(Instant.now().toEpochMilli()).getTime();
        if (exp < now) {
            throw new BizException(AuthException.TOKEN_EXPIRED);
        }
        return true;
    }

    public String getUserNameFromToken(String authToken) {
        Map<String, Object> map;
        try {
            map = JWSObject.parse(authToken).getPayload().toJSONObject();
        } catch (ParseException e) {
            throw new BizException(AuthException.TOKEN_PARSING_USERNAME_ERROR);
        }
        return Convert.toStr(map.get("sub"));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("鉴权失败: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "未鉴权");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
