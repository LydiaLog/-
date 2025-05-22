//package com.bookreum.domain.post.service;
//
//import com.bookreum.domain.book.entity.Book;
//import com.bookreum.domain.post.dto.PostDto;
//import com.bookreum.domain.post.entity.Post;
//import com.bookreum.domain.post.repository.PostRepository;
//import com.bookreum.domain.user.entity.User;
//import org.junit.jupiter.api.BeforeEach; 
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import jakarta.persistence.EntityManager;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class PostServiceTest {
//
//    @Autowired 
//    private PostService postService;
//    
//    @Autowired 
//    private PostRepository postRepository;
//
//    @Autowired 
//    private EntityManager em;
//
//    private User author;
//    private Book book;
//
//    @BeforeEach
//    void setUp() {
//        author = User.builder()
//                .nickname("통합테스터")
//                .profileImage("https://example.com/profile.jpg")
//                .build();
//        
//        book = Book.builder()
//                .title("테스트 북")
//                .author("저자")
//                .coverImageUrl("https://example.com/book.jpg")
//                .build();
//
//        em.persist(author);
//        em.persist(book);
//        em.flush();
//    }
//
//    @Test
//    void createPost_persistsToMySQL() {
//        String title = "첫 번째 글";
//        String content = "본문입니다.";
//        String coverImageUrl = "https://example.com/book.jpg";
//        MockMultipartFile image = new MockMultipartFile("image", new byte[0]);
//
//        PostDto.Response response = postService.createPost(title, content, coverImageUrl, image, author, book);
//
//        em.flush();
//        em.clear();
//
//        Post savedPost = postRepository.findById(response.getId()).orElseThrow();
//
//        assertThat(savedPost).isNotNull();
//        assertThat(savedPost.getTitle()).isEqualTo(title);
//        assertThat(savedPost.getContent()).isEqualTo(content);
//        assertThat(savedPost.getImageUrl()).isEqualTo(coverImageUrl); // 커버 이미지 URL 확인
//        assertThat(savedPost.getUser().getId()).isEqualTo(author.getId());
//        assertThat(savedPost.getBook().getId()).isEqualTo(book.getId());
//    }
//
//    @Test
//    void createPost_throwsExceptionForMissingTitle() {
//        String content = "본문입니다.";
//        String coverImageUrl = "https://example.com/book.jpg";
//        MockMultipartFile image = new MockMultipartFile("image", new byte[0]);
//
//        assertThrows(DataIntegrityViolationException.class, () -> 
//                postService.createPost(null, content, coverImageUrl, image, author, book));
//    }
//
//    @Test
//    void updatePost_updatesSuccessfully() {
//        Post post = Post.builder()
//                .title("기존 제목")
//                .content("기존 내용")
//                .imageUrl("https://example.com/old_image.jpg")
//                .book(book)
//                .user(author)
//                .build();
//        em.persist(post);
//
//        String newTitle = "업데이트된 제목";
//        String newContent = "업데이트된 내용";
//        MockMultipartFile newImage = new MockMultipartFile("image", new byte[0]);
//
//        postService.updatePost(post.getId(), newTitle, newContent, null, newImage);
//
//        em.flush();
//        em.clear();
//
//        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
//
//        assertThat(updatedPost.getTitle()).isEqualTo(newTitle);
//        assertThat(updatedPost.getContent()).isEqualTo(newContent);
//        assertThat(updatedPost.getImageUrl()).isNotNull();
//    }
//}
