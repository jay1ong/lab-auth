package com.im.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.enums.AuthException;
import com.im.exception.BizException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/14
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    private final ObjectMapper objectMapper;

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
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
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

    public boolean validateToken(String authToken) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(authToken);
        JWSVerifier jwsVerifier = new RSASSAVerifier(this.publicKey);
        JWTClaimsSet claims = objectMapper.convertValue(jwsObject.getPayload(), JWTClaimsSet.class);
        if (!jwsObject.verify(jwsVerifier)) {
            throw new BizException(AuthException.INVALID_TOKEN);
        }
        if (claims.getExpirationTime().getTime() < new Date().getTime()) {
            throw new BizException(AuthException.TOKEN_EXPIRED);
        }
        return true;
    }

    public String getUserNameFromToken(String authToken) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(authToken).getPayload().toJWSObject();
        return objectMapper.convertValue(jwsObject.getPayload(), JWTClaimsSet.class).getSubject();
    }
}
