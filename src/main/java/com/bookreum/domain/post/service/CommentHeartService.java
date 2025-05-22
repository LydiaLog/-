package com.bookreum.domain.post.service;

import com.bookreum.domain.post.entity.Comment;
import com.bookreum.domain.post.entity.CommentHeart;
import com.bookreum.domain.post.repository.CommentHeartRepository;
import com.bookreum.domain.post.repository.CommentRepository;
import com.bookreum.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class CommentHeartService {

    private final CommentRepository commentRepository;
    private final CommentHeartRepository commentHeartRepository;

    @Transactional
    public void toggleHeart(Integer commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        CommentHeart existingHeart = commentHeartRepository.findByUserAndComment(user, comment).orElse(null);

        if (existingHeart != null) {
            commentHeartRepository.delete(existingHeart);
        } else {
            commentHeartRepository.save(CommentHeart.builder()
                    .user(user)
                    .comment(comment)
                    .build());
        }
    }

    public boolean hasHeart(Integer commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        return commentHeartRepository.findByUserAndComment(user, comment).isPresent();
    }

    public Long getHeartCount(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        return commentHeartRepository.countByComment(comment);
    }
}
