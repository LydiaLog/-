package com.bookreum.domain.post.entity;

import com.bookreum.domain.book.entity.Book;
import com.bookreum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 255)
    private String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHeart> postHearts = new ArrayList<>();

    // ✅ 공감 수 필드 추가
    @Column(name = "heart_count", nullable = false)
    private long heartCount = 0L;

    // ✅ 생성 시간 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ 게시글 정보 수정 (명확한 Setter 방식)
    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void setContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public void setBook(Book book) {
        if (book != null) {
            this.book = book;
        }
    }

    // ✅ 공감 수 증가
    public void incrementHeartCount() {
        this.heartCount++;
    }

    // ✅ 공감 수 감소
    public void decrementHeartCount() {
        if (this.heartCount > 0) {
            this.heartCount--;
        }
    }
    /** 연관관계 편의 메서드: 사용자 설정 */
    public void setUser(User user) {
        this.user = user;
    }
}
