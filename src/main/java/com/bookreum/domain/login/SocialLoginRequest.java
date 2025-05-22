package com.bookreum.domain.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 전달받은 카카오 JS SDK 로그인 데이터
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    // 카카오에서 받은 Access Token
    private String accessToken;
    // 카카오 고유 사용자 ID
    private String kakaoId;
    // 사용자 닉네임
    private String nickname;
    // 프로필 이미지 URL
    private String profileImageUrl;
}