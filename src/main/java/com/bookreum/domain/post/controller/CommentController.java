package com.bookreum.domain.post.controller;

import com.bookreum.domain.post.dto.CommentDto;
import com.bookreum.domain.post.entity.Comment;
import com.bookreum.domain.post.service.CommentService;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public CommentDto.Response createComment(@RequestBody CommentDto.Request request) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
        Post post = Post.builder().id(request.getPostId()).build();
        return commentService.createComment(request, user, post);
    }

    // 특정 게시글의 댓글 전체 조회 (본인 여부 확인)
    @GetMapping("/post/{postId}")
    public List<CommentDto.Response> getCommentsByPost(
            @PathVariable(name = "postId") Integer postId,
            @RequestParam(name = "isOwner") boolean isOwner) {
        return commentService.getCommentsByPost(postId, isOwner);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable(name = "id") Integer id) {
        commentService.deleteComment(id);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public void updateComment(
            @PathVariable(name = "id") Integer id, 
            @RequestBody CommentDto.Request request) {
        commentService.updateComment(id, request.getContent());
    }
}
