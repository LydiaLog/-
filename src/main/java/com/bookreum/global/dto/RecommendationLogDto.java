package com.bookreum.global.dto;

public class RecommendationLogDto {
    private Long id;
    private Long userId;
    private Long recommendedBookId;
    private Long basePostId;
    private String method;
    private String reason;
    private String createdAt;
}