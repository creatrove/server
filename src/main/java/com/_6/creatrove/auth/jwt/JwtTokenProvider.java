package com._6.creatrove.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMillis = 1000 * 60 * 60 * 24;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Long userId, String email) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    /**
     * 유효하면 정상 반환, 실패 시 원인이 담긴 JwtValidationException을 던진다.
     */
    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("만료된 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("잘못된 형식의 토큰입니다.");
        } catch (SignatureException e) {
            throw new JwtValidationException("토큰 서명이 유효하지 않습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("지원하지 않는 토큰 형식입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("토큰이 비어있습니다.");
        } catch (JwtException e) {
            throw new JwtValidationException("유효하지 않은 토큰입니다.");
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}