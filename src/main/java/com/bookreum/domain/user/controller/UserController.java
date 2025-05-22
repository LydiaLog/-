package com.bookreum.domain.user.controller;

import com.bookreum.domain.post.dto.PostDto;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	//내가 쓴 글 목록
	@GetMapping("/{userId}/posts")
	public List<PostDto.Response> getMyPosts(@PathVariable Integer userId){
		return userService.getPostsByUser(userId);  // 여기 수정
	}
	
	//내가 마음 누른 글 목록
	@GetMapping("/{userId}/liked-posts")
	public List<PostDto.Response> getLikedPosts(@PathVariable Integer userId){
		return userService.getLikedPosts(userId);
	}
}