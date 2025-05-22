package com.bookreum.domain.security;


import io.jsonwebtoken.*;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SecretKey key;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}") 
    private long validityMs;

    public String createAccessToken(String kakaoId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs);
        return Jwts.builder()
                   .setSubject(kakaoId)
                   .setIssuedAt(now)
                   .setExpiration(exp)
                   .signWith(key, SignatureAlgorithm.HS512)
                   .compact();
    }

    public String createRefreshToken(String kakaoId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs * 2);
        return Jwts.builder()
                   .setSubject(kakaoId)
                   .setIssuedAt(now)
                   .setExpiration(exp)
                   .signWith(key, SignatureAlgorithm.HS512)
                   .compact();
    }

    /**
     * JWT로부터 Authentication 객체 생성
     */
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = parse(token);
            String kakaoId = claims.getSubject();
            System.out.println("Creating authentication for user: " + kakaoId);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(kakaoId);
            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        } catch (Exception e) {
            System.out.println("Error creating authentication: " + e.getMessage());
            return null;
        }
    }

    /**
     * JWT 유효성 검증 (만료, 변조 등)
     */
    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            Claims claims = parse(token);
            System.out.println("Token claims: " + claims);
            System.out.println("Token expiration: " + claims.getExpiration());
            
            // 만료 시간 확인
            Date now = new Date();
            if (claims.getExpiration().before(now)) {
                System.out.println("Token is expired");
                return false;
            }
            
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    /**
     * JWT 토큰으로부터 카카오 ID를 추출
     */
    public String getKakaoIdFromToken(String token) {
        try {
            Claims claims = parse(token);
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Error getting kakao ID: " + e.getMessage());
            return null;
        }
    }

    private Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            throw e;
        }
    }
}