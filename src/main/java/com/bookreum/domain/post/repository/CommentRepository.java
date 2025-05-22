package com.bookreum.domain.post.repository;

import com.bookreum.domain.post.entity.Comment;
import com.bookreum.domain.post.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    // ✅ User 정보를 함께 로드하도록 설정 (N+1 문제 예방)
    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByPostId(Integer postId);

    long countByPost(Post post);
}
