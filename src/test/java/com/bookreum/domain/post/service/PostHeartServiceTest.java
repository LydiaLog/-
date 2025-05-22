package com.bookreum.domain.post.service;

import com.bookreum.domain.book.entity.Book;
import com.bookreum.domain.post.entity.Post;
import com.bookreum.domain.post.entity.PostHeart;
import com.bookreum.domain.post.repository.PostHeartRepository;
import com.bookreum.domain.post.repository.PostRepository;
import com.bookreum.domain.user.entity.User;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostHeartServiceTest {

    @Autowired
    private PostHeartRepository postHeartRepository;

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;

    private User author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = User.builder().nickname("통합테스터").build();
        book = Book.builder().title("테스트 북").author("저자").build();

        em.persist(author);
        em.persist(book);
        em.flush();
        em.clear();
    }

    @Test
    void createPostHeart_savesSuccessfully() {
        // given
        Post post = Post.builder()
                .title("테스트 게시글")
                .content("테스트 내용")
                .user(author)
                .book(book)
                .build();

        em.persist(post);

        PostHeart postHeart = PostHeart.builder()
                .user(author)
                .post(post)
                .build();

        // when
        em.persist(postHeart);
        em.flush();

        // then
        assertThat(postHeartRepository.findById(postHeart.getId())).isPresent();
    }
}
