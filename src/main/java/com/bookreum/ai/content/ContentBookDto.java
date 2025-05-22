package com.bookreum.ai.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentBookDto {
    private String isbn13;
    private String title;
    private String author;
    private String coverImageUrl;
    private String description; // ✅ description 필드 추가
}
