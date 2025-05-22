//package com.bookreum.domain.post.controller;
//
//import com.bookreum.domain.post.dto.PostDto;
//import com.bookreum.domain.post.service.PostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/home")
//@RequiredArgsConstructor
//public class MainController {
//
//    private final PostService postService;
//
//    @GetMapping
//    public ResponseEntity<List<PostDto.SimpleResponse>> getHomeData(
//            @RequestParam(name = "sort", defaultValue = "latest") String sort,
//            @RequestParam(name = "keyword", required = false) String keyword,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        List<PostDto.Response> posts = (keyword != null && !keyword.isEmpty()) ? 
//            postService.searchPosts(keyword, sort, pageable) :
//            (sort.equalsIgnoreCase("latest") ? postService.getLatestPosts(pageable) : postService.getOldestPosts(pageable));
//
//        List<PostDto.SimpleResponse> simplePosts = posts.stream().map(PostDto.Response::toSimpleResponse).collect(Collectors.toList());
//
//        return ResponseEntity.ok(simplePosts);
//    }
//
//    @GetMapping("/preview")
//    public ResponseEntity<List<PostDto.SimpleResponse>> getHomePreview() {
//        Pageable pageable = PageRequest.of(0, 2);
//        List<PostDto.Response> posts = postService.getLatestPosts(pageable);
//
//        List<PostDto.SimpleResponse> simplePosts = posts.stream().map(PostDto.Response::toSimpleResponse).collect(Collectors.toList());
//
//        return ResponseEntity.ok(simplePosts);
//    }
//}

package com.bookreum.domain.main;

import com.bookreum.domain.post.dto.PostDto;
import com.bookreum.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    /**
     * 📌 메인 페이지 - 최신 북로그 2개 미리보기
     * @return 최신 북로그 2개 미리보기 데이터
     */
    @GetMapping
    public ResponseEntity<List<PostDto.SimpleResponse>> getMainPagePreview() {
        Pageable pageable = PageRequest.of(0, 2);
        List<PostDto.Response> latestPosts = postService.getLatestPosts(pageable);

        List<PostDto.SimpleResponse> previewPosts = latestPosts.stream().map(post -> PostDto.SimpleResponse.builder()
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

        return ResponseEntity.ok(previewPosts);
    }
}

