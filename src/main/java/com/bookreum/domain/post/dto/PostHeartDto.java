package com.bookreum.domain.post.dto;

import lombok.*;

public class PostHeartDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long postId;
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long userId;
        private Long postId;
        private String createdAt;
        private boolean isLiked;
    }
}
