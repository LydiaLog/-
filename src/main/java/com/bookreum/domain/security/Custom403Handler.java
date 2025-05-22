package com.bookreum.domain.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 403 Forbidden 예외 발생 시 JSON 응답 처리
 */
@Slf4j
@Component
public class Custom403Handler implements AccessDeniedHandler {
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Asia/Seoul"));

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex)
            throws IOException, ServletException {
        log.warn("[403] {} {}", req.getMethod(), req.getRequestURI());
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType("application/json;charset=UTF-8");
        String body = String.format(
            "{\"timestamp\":\"%s\",\"status\":403,\"error\":\"FORBIDDEN\"," +
            "\"message\":\"접근이 거부되었습니다. 권한을 확인하세요.\",\"path\":\"%s\"}",
            FMT.format(Instant.now()), req.getRequestURI());
        res.getWriter().write(body);
    }
}