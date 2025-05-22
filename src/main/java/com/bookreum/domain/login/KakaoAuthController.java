package com.bookreum.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * JS SDK Flow용 카카오 로그인 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService service;

    /**
     * 클라이언트에서 전달한 SocialLoginRequest를 받아
     * JWT 발급 결과를 반환
     */
    @PostMapping("/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(
            @RequestBody SocialLoginRequest req) {
        return ResponseEntity.ok(service.loginWithKakao(req));
    }
}