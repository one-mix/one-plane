// 작성자: 김동현
package com.oneplane.user.service;

import com.oneplane.config.CustomUserDetails;
import com.oneplane.user.dao.UserDao;
import com.oneplane.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static com.oneplane.user.domain.Grade.ECONOMY;
import static com.oneplane.user.domain.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserDao userDao;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("OAuth2 사용자 로드 시작");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();

        log.debug("OAuth2 제공자: {}", registrationId);

        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException("지원하지 않는 로그인 방식입니다: " + registrationId);
        }

        // 카카오 사용자 정보 추출
        KakaoUserInfo kakaoInfo = extractKakaoUserInfo(attributes);

        // 사용자 처리 (신규 가입 또는 기존 사용자 업데이트)
        User user = processOAuth2User(kakaoInfo);

        log.info("OAuth2 로그인 완료 - 사용자: {}, ID: {}", user.getEmail(), user.getUser_id());

        return new CustomUserDetails(user, attributes);
    }

    private KakaoUserInfo extractKakaoUserInfo(Map<String, Object> attributes) {
        try {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");
            String profileImage = (String) profile.get("profile_image_url");

            if (email == null || email.trim().isEmpty()) {
                throw new OAuth2AuthenticationException("카카오 계정에서 이메일을 가져올 수 없습니다. 카카오 계정 설정을 확인해주세요.");
            }

            log.debug("카카오 사용자 정보 추출 완료 - 이메일: {}, 닉네임: {}", email, nickname);

            return new KakaoUserInfo(email, nickname, profileImage);

        } catch (Exception e) {
            log.error("카카오 사용자 정보 추출 실패", e);
            throw new OAuth2AuthenticationException("카카오 사용자 정보를 처리할 수 없습니다.");
        }
    }

    /**
     * OAuth2 사용자 처리 (신규 가입 또는 기존 사용자 로그인)
     */
    private User processOAuth2User(KakaoUserInfo kakaoInfo) {
        User existingUser = userDao.findByEmail(kakaoInfo.getEmail());

        if (existingUser != null) {
            log.info("기존 회원 로그인: {}", kakaoInfo.getEmail());
            // 기존 사용자는 업데이트 없이 그대로 반환
            return existingUser;
        } else {
            log.info("신규 회원 등록: {}", kakaoInfo.getEmail());
            return createNewUserFromKakao(kakaoInfo);
        }
    }

    /**
     * 카카오 OAuth2로 신규 사용자 생성
     */
    private User createNewUserFromKakao(KakaoUserInfo kakaoInfo) {
        User newUser = User.builder()
                .user_id(null)
                .email(kakaoInfo.getEmail())
                .name(null)
                .nickname(null)
                .age(null)
                .role(ROLE_USER)
                .grade(ECONOMY)
                .gender(null)
                .disease(null)
                .disability(null)
                .medication(null)
                .profileImg(kakaoInfo.getProfileImg())
                .deletedAt(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            int result = userDao.insertUser(newUser);
            if (result > 0) {
                User createdUser = userDao.findByEmail(kakaoInfo.getEmail());
                if (createdUser != null) {
                    log.info("신규 사용자 생성 완료 - ID: {}, 이메일: {}", createdUser.getUser_id(), createdUser.getEmail());
                    return createdUser;
                } else {
                    throw new OAuth2AuthenticationException("생성된 사용자 정보를 조회할 수 없습니다.");
                }
            } else {
                throw new OAuth2AuthenticationException("사용자 생성에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("신규 사용자 생성 실패: {}", kakaoInfo.getEmail(), e);
            throw new OAuth2AuthenticationException("사용자 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private static class KakaoUserInfo {
        private final String email;
        private final String nickname;
        private final String profileImg;

        public KakaoUserInfo(String email, String nickname, String profileImg) {
            this.email = email;
            this.nickname = nickname;
            this.profileImg = profileImg;
        }

        public String getEmail() { return email; }
        public String getNickname() { return nickname; }
        public String getProfileImg() { return profileImg; }
    }
}