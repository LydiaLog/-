package com.bookreum.domain.book.repository;

import com.bookreum.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // ISBN으로 책 찾기 (중복 방지)
    Optional<Book> findByTitleAndAuthor(String title, String author);

    // 특정 저자의 책들 찾기
    List<Book> findByAuthorContaining(String author);

    // 제목으로 책 검색 (키워드 기반)
    List<Book> findByTitleContaining(String keyword);

    // 특정 책이 이미 존재하는지 확인 (제목과 저자로)
    boolean existsByTitleAndAuthor(String title, String author);
}
