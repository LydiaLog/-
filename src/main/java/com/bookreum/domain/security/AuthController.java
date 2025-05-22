package com.bookreum.domain.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.repository.UserRepository;
import java.util.Map;

/**
 * OAuth2 로그인 콜백 및 JWT 반환 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * Kakao 로그인 콜백 엔드포인트
     */
    @GetMapping("/kakao/callback")
    public Map<String, String> kakaoLogin(OAuth2AuthenticationToken authentication) {
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        
        String kakaoId = attributes.get("id").toString();
        String nickname = ((Map<String, Object>) attributes.get("properties")).get("nickname").toString();
        String profileImage = ((Map<String, Object>) attributes.get("properties")).get("profile_image").toString();

        // 사용자 정보 저장 또는 업데이트
        User user = userRepository.findByKakaoId(kakaoId)
            .map(u -> {
                u.updateProfile(nickname, profileImage);
                return userRepository.save(u);
            })
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .kakaoId(kakaoId)
                    .nickname(nickname)
                    .profileImage(profileImage)
                    .build()
            ));

        // JWT 토큰 생성
        String token = jwtTokenProvider.createAccessToken(kakaoId);
        return Map.of("token", token);
    }
}