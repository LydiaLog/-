//package com.bookreum.domain.post.service;
//
//import com.bookreum.domain.post.dto.CommentDto;
//import com.bookreum.domain.post.entity.Comment;
//import com.bookreum.domain.post.entity.Post;
//import com.bookreum.domain.post.repository.CommentRepository;
//import com.bookreum.domain.post.repository.PostRepository;
//import com.bookreum.domain.user.entity.User;
//import com.bookreum.domain.user.repository.UserRepository;
//import com.bookreum.domain.book.entity.Book;
//import com.bookreum.domain.book.repository.BookRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class CommentServiceTest {
//
//    @Autowired
//    private CommentService commentService;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @PersistenceContext
//    private EntityManager em;
//
//    private User user;
//    private Post post;
//    private Book book;
//    private Integer postId;
//
//    @BeforeEach
//    void setUp() {
//        user = userRepository.save(User.builder().nickname("테스트 사용자").build());
//        book = bookRepository.save(Book.builder().title("테스트 책").author("테스트 저자").build());
//        post = postRepository.save(Post.builder().title("테스트 글").content("테스트 내용").user(user).book(book).build());
//        postId = post.getId();
//        em.flush();
//        em.clear();
//    }
//
//    @Test
//    void createComment_persistsToDatabase() {
//        CommentDto.Request request = new CommentDto.Request();
//        request.setPostId(postId);
//        request.setContent("댓글 내용");
//
//        CommentDto.Response response = commentService.createComment(request, user, post);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getContent()).isEqualTo("댓글 내용");
//        assertThat(response.getAuthorName()).isEqualTo(user.getNickname());
//    }
//
//    @Test
//    void getAllCommentsForPost() {
//        CommentDto.Request request1 = new CommentDto.Request();
//        request1.setPostId(postId);
//        request1.setContent("첫 번째 댓글");
//        commentService.createComment(request1, user, post);
//
//        CommentDto.Request request2 = new CommentDto.Request();
//        request2.setPostId(postId);
//        request2.setContent("두 번째 댓글");
//        commentService.createComment(request2, user, post);
//
//        boolean isOwner = true;
//        List<CommentDto.Response> comments = commentService.getCommentsByPost(postId, isOwner);
//
//        assertThat(comments).hasSize(2);
//    }
//
//    @Test
//    void deleteComment_removesFromDatabase() {
//        CommentDto.Request request = new CommentDto.Request();
//        request.setPostId(postId);
//        request.setContent("댓글 내용");
//        CommentDto.Response response = commentService.createComment(request, user, post);
//        commentService.deleteComment(response.getId());
//
//        assertThat(commentRepository.findAll()).isEmpty();
//    }
//
//    @Test
//    void updateComment_changesContent() {
//        CommentDto.Request request = new CommentDto.Request();
//        request.setPostId(postId);
//        request.setContent("원래 댓글");
//        CommentDto.Response response = commentService.createComment(request, user, post);
//        commentService.updateComment(response.getId(), "수정된 댓글");
//
//        Comment updatedComment = commentRepository.findById(response.getId()).orElseThrow();
//        assertThat(updatedComment.getContent()).isEqualTo("수정된 댓글");
//    }
//}
