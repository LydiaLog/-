package com.bookreum.domain.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain)
              throws ServletException, IOException {
        
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("Processing request for path: {} [{}]", path, method);

        if (path.startsWith("/api/auth/")
        	    || path.equals("/api/home")
        	    || path.equals("/api/main")
        	    || path.equals("/api/aladin/search")
        	    || path.equals("/api/books/search")
        	    || path.equals("/api/posts/saveBook")
        	    || (method.equals("GET") && path.matches("/api/posts"))
        	    || (method.equals("GET") && path.matches("/api/posts/\\d+"))
        	    || (method.equals("GET") && path.matches("/api/comments/post/\\d+"))
        	    || path.equals("/api/clubs/saveBook")
        	    || path.equals("/api/clubs/searchBooks")
        	    || path.startsWith("/api/clubs/public/")
        	    || path.equals("/api/clubs/public")
        	    || path.equals("/api/clubs/public/latest")
        	    || path.equals("/api/clubs/public?includeBook=true")
        	    || (method.equals("GET") && path.equals("/api/clubs"))
        	    || (method.equals("GET") && path.matches("/api/clubs/\\d+"))) {
        	    
        	    log.info("Public API path, skipping token validation");
        	    filterChain.doFilter(request, response);
        	    return;
        	}


        // 2) 그 외 요청은 헤더에서 Bearer 토큰을 추출하여 유효성 검사
        String header = request.getHeader("Authorization");
        log.info("Authorization header: {}", header);
        
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            log.warn("No valid Authorization header found");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No token provided");
            return;
        }

        String token = header.substring(7);
        log.info("Extracted token: {}", token);
        
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("Token validation failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        try {
            String kakaoId = jwtTokenProvider.getKakaoIdFromToken(token);
            log.info("Kakao ID from token: {}", kakaoId);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(kakaoId);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("Authentication set in SecurityContext");
            
            // 인증 성공 시 다음 필터로 진행
            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            log.warn("JWT 토큰의 subject에 해당하는 유저가 없습니다: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 오류 발생: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token processing error");
        }
    }
}