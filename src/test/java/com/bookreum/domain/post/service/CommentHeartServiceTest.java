package com.bookreum.domain.post.service;

import com.bookreum.domain.post.entity.Comment;
import com.bookreum.domain.post.entity.CommentHeart;
import com.bookreum.domain.post.entity.Post;
import com.bookreum.domain.post.repository.CommentHeartRepository;
import com.bookreum.domain.post.repository.CommentRepository;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.repository.UserRepository;
import com.bookreum.domain.book.entity.Book;
import com.bookreum.domain.book.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional
public class CommentHeartServiceTest {

    @Autowired
    private CommentHeartService commentHeartService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PersistenceContext
    private EntityManager em;

    private User author;
    private Post post;
    private Comment comment;
    private Book book;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        author = User.builder().nickname("테스트 사용자").build();
        em.persist(author);

        // 테스트용 책 생성
        book = Book.builder()
                .title("테스트 책")
                .author("테스트 저자")
                .coverImageUrl("https://example.com/cover.jpg")
                .build();
        em.persist(book);

        // 테스트용 게시글 생성 (Post 먼저 저장)
        post = Post.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .user(author)
                .book(book) // 책 연결
                .build();
        em.persist(post);

        // 댓글 생성
        comment = Comment.builder()
                .content("테스트 댓글")
                .post(post)  // 여기에서 Post를 설정
                .user(author)
                .build();
        em.persist(comment);

        em.flush();
        em.clear();
    }

    @Test
    void toggleHeart_addAndRemoveHeart() {
        // 공감 추가
        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartRepository.countByComment(comment)).isEqualTo(1);

        // 공감 취소
        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartRepository.countByComment(comment)).isEqualTo(0);
    }

    @Test
    void hasHeart_checkIfHeartExists() {
        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartService.hasHeart(comment.getId(), author)).isTrue();

        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartService.hasHeart(comment.getId(), author)).isFalse();
    }

    @Test
    void getHeartCount_correctCount() {
        assertThat(commentHeartService.getHeartCount(comment.getId())).isEqualTo(0);

        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartService.getHeartCount(comment.getId())).isEqualTo(1);

        commentHeartService.toggleHeart(comment.getId(), author);
        assertThat(commentHeartService.getHeartCount(comment.getId())).isEqualTo(0);
    }
}
