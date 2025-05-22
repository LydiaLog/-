package com.bookreum.domain.post.repository;

import com.bookreum.domain.post.entity.CommentHeart;
import com.bookreum.domain.post.entity.Comment;
import com.bookreum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Integer> {

    // 특정 유저가 특정 댓글에 공감했는지 확인
    Optional<CommentHeart> findByUserAndComment(User user, Comment comment);

    // 댓글에 달린 공감 모두 조회
    List<CommentHeart> findAllByComment(Comment comment);

    // 댓글 ID로 공감 수 세기
    Long countByComment(Comment comment);

    // 특정 댓글의 모든 공감 삭제 (댓글 삭제 시 cascade 안 쓴 경우 유용)
    void deleteAllByComment(Comment comment);
}
