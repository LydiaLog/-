package com.bookreum.domain.post.entity;

import com.bookreum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ CommentHeart와의 연관 관계 설정
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentHeart> commentHearts = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ 안전한 Setter 메소드 추가 (보호된 접근)
    public void setContent(String content) {
        this.content = content;
    }

    // ✅ 내용 업데이트 메소드 (권장)
    public void updateContent(String newContent) {
        this.content = newContent;
    }

    // ✅ 댓글 삭제 시 자동으로 관련 좋아요 삭제
    public void addCommentHeart(CommentHeart commentHeart) {
        commentHearts.add(commentHeart);
        commentHeart.setComment(this);
    }

    public void removeCommentHeart(CommentHeart commentHeart) {
        commentHearts.remove(commentHeart);
        commentHeart.setComment(null);
    }
}
