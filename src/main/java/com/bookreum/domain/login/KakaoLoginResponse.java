package com.bookreum.domain.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 성공 후 반환되는 JWT 및 프로필 정보 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {
    // 서비스용 JWT Access Token
    private String accessToken;
    // 서비스용 JWT Refresh Token
    private String refreshToken;
    // 내부 사용자 PK
    private Integer userId;
    // 사용자 닉네임
    private String nickname;
    // 프로필 이미지 URL
    private String profileImageUrl;
}