package com.bookreum.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bookreum.domain.user.entity.User;

/**
 * 사용자 정보를 외부에 노출하기 위한 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    private Integer id;
    private String  kakaoId;
    private String  nickname;
    private String  profileImage;

    /** 소셜 가입 여부  (기본값 false) */
    @Builder.Default
    private boolean social = true;   // 모든 사용자가 소셜 가입



    /* ---------- 변환 메서드 ---------- */

    /** Entity → DTO */
    public static UserDto fromEntity(User e) {
        return UserDto.builder()
                      .id(e.getId())
                      .kakaoId(e.getKakaoId())
                      .nickname(e.getNickname())
                      .profileImage(e.getProfileImage())
                      .social(e.isSocial())
                      .build();
    }

    /** DTO → Entity (필요할 때 사용) */
    public User toEntity() {
        return User.builder()
                         .id(id)
                         .kakaoId(kakaoId)
                         .nickname(nickname)
                         .profileImage(profileImage)
                         .social(social)
                         .build();
    }
}
