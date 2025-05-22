package com.bookreum.ai.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long> {
    
    // 기존 findByTitle 메서드 유지
    Optional<RecommendedBook> findByTitleAndAuthor(String title, String author);
    
    // 존재 여부 확인 메서드 추가 (Optional 사용)
    boolean existsByTitleAndAuthor(String title, String author);
}
