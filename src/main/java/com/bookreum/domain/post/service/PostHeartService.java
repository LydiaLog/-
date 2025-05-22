package com.bookreum.domain.post.service;

import com.bookreum.domain.post.entity.Post;
import com.bookreum.domain.post.entity.PostHeart;
import com.bookreum.domain.post.repository.PostHeartRepository;
import com.bookreum.domain.post.repository.PostRepository;
import com.bookreum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostHeartService {

    private final PostHeartRepository postHeartRepository;
    private final PostRepository postRepository;

    /**
     * ✅ 공감 토글 (추가 또는 취소)
     * @param postId 공감을 추가할 게시글 ID
     * @param user 공감을 추가하는 사용자
     * @return true -> 공감 추가됨, false -> 공감 취소됨
     */
    @Transactional
    public boolean toggleHeart(Integer postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("📌 해당 게시글을 찾을 수 없습니다."));

        // ✅ 기존 공감 여부 확인
        PostHeart existingHeart = postHeartRepository.findByUserAndPost(user, post).orElse(null);

        if (existingHeart != null) {
            postHeartRepository.delete(existingHeart);
            System.out.println("✅ 공감 취소됨: 사용자 ID = " + user.getId() + ", 게시글 ID = " + postId);
            return false;
        }

        // ✅ 공감 추가 (DB에 저장)
        PostHeart heart = PostHeart.builder()
                .user(user)
                .post(post)
                .build();
        postHeartRepository.save(heart);
        System.out.println("✅ 공감 추가됨: 사용자 ID = " + user.getId() + ", 게시글 ID = " + postId);
        return true;
    }
    
    /**
     * ✅ 사용자가 특정 게시글에 좋아요를 눌렀는지 확인 (임시 사용자)
     * @param postId 게시글 ID
     * @param user 사용자 (임시 사용자)
     * @return true -> 좋아요 누름, false -> 좋아요 안 누름
     */
    @Transactional(readOnly = true)
    public boolean isHearted(Integer postId, User user) {
        return postHeartRepository.findByUserAndPost(user, postRepository.findById(postId).orElse(null)).isPresent();
    }

    /**
     * ✅ 특정 게시글의 공감 수 조회
     * @param postId 공감 수를 조회할 게시글 ID
     * @return 공감 수 (long)
     */
 // ✅ 특정 게시글의 공감 수 조회 (JPQL 쿼리 사용)
    public Integer countHearts(Integer postId) {
    	System.out.println("✅ countHearts 호출됨, postId: " + postId);
    	//성능 최적화 -> DB 직접 조회
        return postHeartRepository.countByPostId(postId);
    }

}
