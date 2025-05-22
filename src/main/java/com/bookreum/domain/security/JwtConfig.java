package com.bookreum.domain.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import java.util.Base64;

/**
 * JWT SecretKey를 생성하고,
 * 생성 시점에 Base64 형태로 로깅합니다.
 */
@Configuration
@Slf4j
public class JwtConfig {


    /**
     * SecretKey 빈 생성 및 로깅
     */
	@Bean
	public SecretKey jwtSecretKey(@Value("${jwt.base64-secret}") String base64Secret) {
	    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
	    return Keys.hmacShaKeyFor(keyBytes); // HS512 에도 충분한 길이
	}


}