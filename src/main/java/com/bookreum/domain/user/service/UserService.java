package com.bookreum.domain.user.service;

import com.bookreum.domain.post.dto.PostDto;
import com.bookreum.domain.post.entity.PostHeart;
import com.bookreum.domain.post.repository.PostHeartRepository;
import com.bookreum.domain.post.repository.PostRepository;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;

    public List<PostDto.Response> getPostsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return postRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(post -> {
                    long heartCount = postHeartRepository.countByPostId(post.getId());
                    long commentCount = 0L; // 댓글 수는 여기서 계산 안 함 (필요시 주입)
                    return PostDto.Response.fromEntity(post, user, heartCount, commentCount);
                })
                .collect(Collectors.toList());
    }

    public List<PostDto.Response> getLikedPosts(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return postHeartRepository.findByUser(user).stream()
                .map(PostHeart::getPost)
                .map(post -> PostDto.Response.fromEntity(post, user, null, null))
                .collect(Collectors.toList());
    }
}
