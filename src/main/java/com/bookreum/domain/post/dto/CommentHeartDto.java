package com.bookreum.domain.post.dto;

import com.bookreum.domain.post.entity.CommentHeart;
import lombok.*;

public class CommentHeartDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long userId;
        private Long commentId;
    }

    @Getter
    @Builder
    public static class Response {
        private Integer id;
        private Integer userId;
        private Integer commentId;
        private String createdAt;

        public static Response fromEntity(CommentHeart entity) {
            return Response.builder()
                    .id(entity.getId())
                    .userId(entity.getUser().getId())
                    .commentId(entity.getComment().getId())
                    .createdAt(entity.getCreatedAt().toString())
                    .build();
        }
    }
}
