package com.bookreum.domain.post.repository;

import com.bookreum.domain.post.entity.Post;
import com.bookreum.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // ✅ 최신순 조회 (페이지네이션 적용)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // ✅ 오래된순 조회 (페이지네이션 적용)
    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

    // ✅ 검색 - 최신순 (페이지네이션 적용)
    @Query("SELECT p FROM Post p JOIN p.book b " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> searchByKeywordOrderByLatest(@Param("kw") String keyword, Pageable pageable);

    // ✅ 검색 - 오래된순 (페이지네이션 적용)
    @Query("SELECT p FROM Post p JOIN p.book b " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "ORDER BY p.createdAt ASC")
    Page<Post> searchByKeywordOrderByOldest(@Param("kw") String keyword, Pageable pageable);

    // ✅ 내가 쓴 글 조회 (최신순)
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    // ✅ 내가 마음 누른 글 조회 (내가 좋아요 누른 글 필터링)
    @Query("SELECT p FROM Post p WHERE EXISTS (" +
            "SELECT ph FROM PostHeart ph WHERE ph.post = p AND ph.user = :user)")
    List<Post> findPostsLikedByUser(@Param("user") User user);

    // ✅ 마이페이지 - 사용자 게시물 조회
    List<Post> findByUser(User user);
    
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user u JOIN FETCH p.book b ORDER BY p.createdAt DESC",
    	       countQuery = "SELECT COUNT(p) FROM Post p")
    	Page<Post> findAllWithUserAndBook(Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN FETCH p.user u JOIN FETCH p.book b WHERE p.id = :postId")
    Optional<Post> findByIdWithUserAndBook(@Param("postId") Integer postId);


}
