package com.bookreum.domain.post.controller;

import com.bookreum.domain.post.dto.PostDto;
import com.bookreum.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    /**
     * 📌 북로그 메인 페이지 - 정렬 및 검색 지원 (키워드 기반)
     * @param sort 정렬 방식 (latest: 최신순, oldest: 오래된순)
     * @param keyword 검색 키워드 (제목, 작성자, 책 제목, 책 저자)
     * @param page 페이지 번호
     * @param size 페이지 당 게시글 수
     * @return 북로그 목록 (카드 형식)
     */
    @GetMapping
    public ResponseEntity<List<PostDto.SimpleResponse>> getHomeData(
            @RequestParam(name = "sort", defaultValue = "latest") String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<PostDto.Response> posts;

        // 📌 검색 조건이 있는 경우 - 키워드 기반 검색
        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(keyword, sort, pageable);
        } else {
            posts = sort.equalsIgnoreCase("latest") 
                ? postService.getLatestPosts(pageable)
                : postService.getOldestPosts(pageable);
        }

        // 📌 프론트에 필요한 데이터 형식으로 변환 (카드 형식)
        List<PostDto.SimpleResponse> simplePosts = posts.stream().map(post -> PostDto.SimpleResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent().length() > 140 ? post.getContent().substring(0, 140) + "…" : post.getContent())
                .date(post.getCreatedAt())
                .bookTitle(post.getBookTitle())
                .bookAuthor(post.getBookAuthor())
                .coverUrl(post.getBookCoverImageUrl())
                .nickname(post.getAuthorName())
                .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(simplePosts);
    }

    /**
     * 📌 북로그 작성 페이지로 이동
     * @return 북로그 작성 페이지
     */
    @GetMapping("/write")
    public ResponseEntity<String> getWritePage() {
        return ResponseEntity.ok("Redirect to Write Page");
    }
}
