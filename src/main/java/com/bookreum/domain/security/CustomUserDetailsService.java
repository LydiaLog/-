package com.bookreum.domain.security;

import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * UserDetailsService 구현체
 * DB에서 사용자 엔티티를 조회하여 Spring Security UserDetails 생성
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 카카오 ID로 사용자 조회
        User user = userRepository.findByKakaoId(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with kakaoId: " + id));

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            "", // 소셜 로그인 비밀번호 사용 안 함
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}