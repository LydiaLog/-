package com.bookreum.domain.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService; 
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://10.50.216.86:5173",
                "http://192.168.0.1:5173",
                "http://192.168.1.1:5173",
                "http://10.50.226.30:5173"
            ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          // 1) CORS
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

          // 2) CSRF 비활성화
          .csrf(csrf -> csrf.disable())

          // 3) 세션 없이 JWT 사용
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

          // 4) 예외 처리
          .exceptionHandling(ex -> ex
              .accessDeniedHandler(accessDeniedHandler)
              .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
          )
          // 6) JWT 인증 필터 등록
          .addFilterBefore(
                  new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                  UsernamePasswordAuthenticationFilter.class
          )

          // 5) URL 권한 설정
          .authorizeHttpRequests(auth -> auth
        	  .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        	  
        	// 카카오 로그인 관련 엔드포인트
              .requestMatchers("/api/auth/kakao", "/api/auth/kakao/**").permitAll()
              .requestMatchers("/api/auth/refresh", "/api/auth/refresh/**").permitAll()
              
              // 공개 API
              .requestMatchers("/api/auth/**", "/api/home").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/aladin/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
              
              // 북로그 조회
              .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/posts/{id}").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/posts/searchBooks").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/posts/search").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/posts/comments/{postId}").permitAll()
              
              // 북클럽 조회
              .requestMatchers(HttpMethod.GET, "/api/clubs/public/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/clubs/public/").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/clubs").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/clubs/{id}").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/clubs/searchBooks").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/clubs/{id}/join").authenticated()
              .requestMatchers(HttpMethod.POST, "/api/clubs/{id}/applications").authenticated()
              
              
              // 채팅 관련 API
              .requestMatchers(HttpMethod.GET, "/api/clubs/{id}/messages").authenticated()
              .requestMatchers(HttpMethod.POST, "/api/clubs/{id}/messages").authenticated()
              .requestMatchers(HttpMethod.DELETE, "/api/clubs/{id}/messages/{messageId}").authenticated()
              
              // 책 저장 API
              .requestMatchers(HttpMethod.POST, "/api/posts/saveBook").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/clubs/saveBook").permitAll()
              
              // 북로그 CRUD
              .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
              .requestMatchers(HttpMethod.PUT, "/api/posts/{id}").authenticated()
              .requestMatchers(HttpMethod.DELETE, "/api/posts/{id}").authenticated()
              
              // 북클럽 CRUD
              .requestMatchers(HttpMethod.POST, "/api/clubs").authenticated()
              .requestMatchers(HttpMethod.PUT, "/api/clubs/{id}").authenticated()
              .requestMatchers(HttpMethod.DELETE, "/api/clubs/{id}").authenticated()
              .requestMatchers(HttpMethod.POST, "/api/clubs/{id}/join").authenticated()
              .requestMatchers(HttpMethod.POST, "/api/clubs/{id}/applications").authenticated()
              
              // 댓글 관련
              .requestMatchers(HttpMethod.GET, "/api/comments/post/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
              .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
              .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()

              // 댓글 좋아요
              .requestMatchers(HttpMethod.POST, "/api/comments/{commentId}/hearts").authenticated()
              .requestMatchers(HttpMethod.GET, "/api/comments/{commentId}/hearts/exists").authenticated()
              .requestMatchers(HttpMethod.GET, "/api/comments/{commentId}/hearts/count").authenticated()

              // 북로그 좋아요 (기존 유지)
              .requestMatchers(HttpMethod.POST, "/api/posts/{id}/hearts").authenticated()
              .requestMatchers(HttpMethod.GET, "/api/posts/{id}/heart").authenticated()
              .requestMatchers(HttpMethod.GET, "/api/posts/{id}/heart-count").authenticated()

              // 메인 페이지
              .requestMatchers(HttpMethod.GET, "/api/main").permitAll()

              // 마이페이지, AI 추천
              .requestMatchers("/api/mypage/**", "/api/reommend/**").authenticated()
              
              // 그 외 모든 요청 인증 필요
              .anyRequest().authenticated()
          );

      

        return http.build();
    }
}