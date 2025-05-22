package com.bookreum.ai.content;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // 🔑 추가
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // ✅ 로그인한 사용자만 접근 허용
public class ContentBasedController {

    private static final Logger logger = LoggerFactory.getLogger(ContentBasedController.class);
    private final ContentBasedService contentBasedService;

    @GetMapping("/title")
    public List<ContentBookDto> recommendByTitle(@RequestParam(name = "title") String title) {
        logger.info("📌 [Title-Based Recommendation] Requested Title: {}", title);

        if (title == null || title.trim().isEmpty()) {
            logger.warn("❌ [Title-Based Recommendation] 제목이 비어있습니다.");
            throw new IllegalArgumentException("제목을 입력해 주세요.");
        }

        try {
            List<ContentBookDto> recommendedBooks = contentBasedService.recommendByTitle(title);
            logger.info("✅ [Title-Based Recommendation] Total Recommended Books: {}", recommendedBooks.size());
            return recommendedBooks;
        } catch (Exception e) {
            logger.error("❌ [Title-Based Recommendation] 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("추천 도중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    @GetMapping("/content")
    public List<ContentBookDto> recommendContentBased(@RequestParam(name = "title") String title) {
        logger.info("📌 [Content-Based AI Recommendation] Requested Title: {}", title);

        if (title == null || title.trim().isEmpty()) {
            logger.warn("❌ [Content-Based AI Recommendation] 제목이 비어있습니다.");
            throw new IllegalArgumentException("제목을 입력해 주세요.");
        }

        try {
            List<ContentBookDto> recommendedBooks = contentBasedService.recommendContentBased(title);
            logger.info("✅ [Content-Based AI Recommendation] Total Recommended Books: {}", recommendedBooks.size());
            return recommendedBooks;
        } catch (Exception e) {
            logger.error("❌ [Content-Based AI Recommendation] 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("서버에서 추천을 처리하는 중 오류가 발생했습니다.");
        }
    }
}