package com.rao.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private final String secret;
    private final SecretKey key;

    public JwtUtil(String secret) {
        this.secret = secret;
        this.key = getSigningKey();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .issuedAt(new Date())
                .compact();//默认永不过期
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String getUserIdFromToken(String token) {
        return (String) parseToken(token).get("userId");
    }

}
