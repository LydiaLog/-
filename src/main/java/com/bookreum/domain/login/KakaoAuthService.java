package com.bookreum.domain.login;

import com.bookreum.domain.user.entity.User;
import com.bookreum.domain.user.repository.UserRepository;
import com.bookreum.domain.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JS SDK에서 전달된 카카오 정보로 로그인 처리하고,
 * JWT를 발급하여 결과 DTO로 반환하는 서비스
 */
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * SocialLoginRequest로 로그인 처리
     * @param req 클라이언트가 전달한 카카오 토큰/프로필 정보
     * @return 발급된 JWT 및 사용자 정보
     */
    @Transactional
    public KakaoLoginResponse loginWithKakao(SocialLoginRequest req) {
        String kakaoId = req.getKakaoId();
        String nick    = req.getNickname();
        String img     = req.getProfileImageUrl();

        // DB 동기화: 기존 사용자 프로필 업데이트 또는 신규 생성
        User user = userRepository.findByKakaoId(kakaoId)
            .map(u -> {
                u.updateProfile(nick, img);
                return userRepository.save(u);
            })
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .social(true)
                    .kakaoId(kakaoId)
                    .nickname(nick)
                    .profileImage(img)
                    .build()
            ));

        // JWT Access/Refresh Token 발급
        String jwtAcc = jwtTokenProvider.createAccessToken(kakaoId);
        String jwtRef = jwtTokenProvider.createRefreshToken(kakaoId);

        // 응답 DTO 생성
        return KakaoLoginResponse.builder()
            .accessToken(jwtAcc)
            .refreshToken(jwtRef)
            .userId(user.getId())
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImage())
            .build();
    
    }
}