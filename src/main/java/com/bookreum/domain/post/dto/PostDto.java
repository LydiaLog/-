package com.bookreum.domain.post.dto;

import com.bookreum.domain.post.entity.Post;
import com.bookreum.domain.user.entity.User;
import java.time.format.DateTimeFormatter;
import lombok.*;
import java.util.List;

public class PostDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String content;
        private String imageUrl;
        private Integer bookId; // 📌 Book ID 추가
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Integer id;
        private String title;
        private String content;
        private String imageUrl;
        private String authorName;
        private String bookTitle;
        private String bookAuthor;
        private String bookCoverImageUrl;
        private Integer bookId; // 📌 Book ID 추가
        private String createdAt;
        private Long heartCount;
        private Long commentCount;

        /**
         * 📌 Post 엔티티로부터 Response 생성
         */
        public static Response fromEntity(Post post, User viewer, Long heartCount, Long commentCount) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            return Response.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .authorName(post.getUser().getNickname())
                    .bookTitle(post.getBook().getTitle())
                    .bookAuthor(post.getBook().getAuthor())
                    .bookCoverImageUrl(post.getBook().getCoverImageUrl())
                    .bookId(post.getBook().getId()) // 📌 Book ID 추가
                    .createdAt(post.getCreatedAt().format(formatter))
                    .heartCount(heartCount != null ? heartCount : 0L)
                    .commentCount(commentCount != null ? commentCount : 0L)
                    .build();
        }

        /**
         * 📌 SimpleResponse 생성 (카드 형식)
         */
        public static SimpleResponse toSimpleResponse(Response response) {
            return SimpleResponse.builder()
                    .id(response.getId())
                    .title(response.getTitle())
                    .content(response.getContent().length() > 140 ? response.getContent().substring(0, 140) + "…" : response.getContent())
                    .date(response.getCreatedAt())
                    .bookTitle(response.getBookTitle())
                    .bookAuthor(response.getBookAuthor())
                    .coverUrl(response.getBookCoverImageUrl())
                    .nickname(response.getAuthorName())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class DetailResponse {
        private Integer id;
        private String title;
        private String content;
        private String imageUrl;
        private String nickname;
        private String bookTitle;
        private String bookAuthor;
        private String CoverUrl;
        private Integer bookId; 
        private String date;
        private Long heartCount;
        private List<CommentDto.Response> comments;

        /**
         * 📌 Post 엔티티로부터 DetailResponse 생성
         */
        public static DetailResponse fromEntity(Post post, List<CommentDto.Response> comments, long heartCount, User viewer) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            return DetailResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .nickname(post.getUser().getNickname())
                    .bookTitle(post.getBook() != null ? post.getBook().getTitle() : "Unknown Book Title")
                    .bookAuthor(post.getBook() != null ? post.getBook().getAuthor() : "Unknown Author")
                    .CoverUrl(post.getBook() != null ? post.getBook().getCoverImageUrl() : null)
                    .bookId(post.getBook() != null ? post.getBook().getId() : null)
                    .date(post.getCreatedAt().format(formatter)) // 시간까지 포함된 형식으로
                    .heartCount(heartCount)
                    .comments(comments)
                    .build();
        }
   }

    @Getter
    @Builder
    public static class SimpleResponse {
        private Integer id;
        private String title;
        private String content;
        private String date;
        private String bookTitle;
        private String bookAuthor;
        private String coverUrl;
        private String nickname;
    }
}