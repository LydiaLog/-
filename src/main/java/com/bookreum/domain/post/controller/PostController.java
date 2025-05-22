package com.bookreum.domain.post.controller;

import com.bookreum.domain.post.dto.PostDto;
import com.bookreum.domain.post.service.PostService;
import com.bookreum.domain.book.entity.Book;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.book.repository.BookRepository;
import com.bookreum.external.aladin.AladinBookService;
import com.bookreum.external.aladin.AladinSearchResult;
import com.bookreum.external.aladin.AladinItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AladinBookService aladinBookService;
    private final BookRepository bookRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * 📌 키워드로 책 검색 (DB 저장 X, 검색 결과만 반환)
     * @param keyword 검색할 키워드 (책 제목 또는 저자)
     * @return 검색된 책 목록 (List<AladinItem>)
     */
    @GetMapping("/searchBooks")
    public ResponseEntity<List<AladinItem>> searchBooks(@RequestParam String keyword) {
        AladinSearchResult result = aladinBookService.searchBooks(keyword);

        // 검색 결과가 없으면 404 에러 반환
        if (result.getTotalResults() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found with the given keyword.");
        }
        return ResponseEntity.ok(result.getItem());
    }

    /**
     * 📌 사용자가 선택한 책 저장 (DB에 중복 저장 방지)
     * @param selectedItem 사용자가 선택한 책 정보 (AladinItem)
     * @return 저장된 책 정보 (Book)
     */
    @PostMapping("/saveBook")
    @Transactional
    public ResponseEntity<Book> saveSelectedBook(@RequestBody AladinItem selectedItem) {
        Book existingBook = bookRepository.findByTitleAndAuthor(
                selectedItem.getTitle(), selectedItem.getAuthor()
        ).orElse(null);

        // 기존 책이 있다면 그대로 반환
        if (existingBook != null) {
            return ResponseEntity.ok(existingBook);
        }

        // 새로운 책 저장
        Book newBook = Book.builder()
                .title(selectedItem.getTitle())
                .author(selectedItem.getAuthor())
                .coverImageUrl(selectedItem.getCover())
                .build();
        return ResponseEntity.ok(bookRepository.save(newBook));
    }

    /**
     * 📌 게시글 생성 (사용자가 선택한 책 ID 사용)
     */
    @PostMapping
    @Transactional
    public ResponseEntity<PostDto.Response> createPost(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "bookId") Integer bookId,
            @RequestParam(value = "coverUrl", required = false) String coverUrl,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        System.out.println("📌 createPost 호출됨");

     // 로그인한 사용자 정보 (OAuth2)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        
        // 사용자가 선택한 책 ID로 책 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with ID " + bookId + " not found"));

        // 최종 커버 이미지 URL 결정
        String finalCoverImageUrl = postService.determineCoverImageUrl(coverImage, coverUrl, book);
        
        // 게시글 생성 및 저장
        return ResponseEntity.ok(postService.createPost(title, content, finalCoverImageUrl, coverImage, user, book));
    }

    /**
     * 📌 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable("id") Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "bookId", required = false) Integer bookId, // ✅ bookId는 Optional로 처리
            @RequestParam(value = "coverUrl", required = false) String coverUrl,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        
        postService.updatePost(id, title, content, bookId, coverImage, coverUrl);
        return ResponseEntity.ok().build();
    }

    /**
     * 📌 게시글 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDto.DetailResponse> getPostById(@PathVariable("id") Integer id) {
        System.out.println("📌 Requested Post ID: " + id);
        return ResponseEntity.ok(postService.getPostDetail(id));
    }

    /**
     * 📌 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
