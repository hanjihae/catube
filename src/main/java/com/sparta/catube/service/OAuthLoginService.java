package com.sparta.catube.service;

import com.sparta.catube.entity.User;
import com.sparta.catube.oauth.*;
import com.sparta.catube.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    @Transactional
    public AuthTokens login(OAuthLoginParams params) {
        // Kakao에서 사용자 정보 요청 - Access Token 가져오기
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        // Kakao에서 받은 사용자 정보를 이용하여 회원 조회 및 생성
        Long userId = findOrCreateUser(oAuthInfoResponse);
        // AccessToken 생성
        AuthTokens authTokens = authTokensGenerator.generate(userId);

        // AuthTokens 객체에 포함된 refreshToken을 userRepository를 통해 저장
        String refreshToken = "Bearer " + authTokens.getRefreshToken();
        userRepository.saveUserRefreshToken(userId, refreshToken);

        return authTokens;
    }

    @Transactional
    public AuthTokens regenerateAccessToken(Long userId, String providedRefreshToken) {
        String refreshToken = userRepository.findRefreshTokenByUserId(userId);
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("저장된 리프레시 토큰을 찾을 수 없습니다."));
        if (providedRefreshToken.equals(refreshToken)) {
            // 리프레시 토큰이 일치하면 새로운 Access 토큰 발급
            AuthTokens authTokens = authTokensGenerator.generate(user.getUserId());
            return authTokens;
        } else {
            // 리프레시 토큰이 일치하지 않으면 로그아웃 처리 후 예외 발생
            user.setRefreshToken(null); // 혹은 다른 방식으로 만료 처리 로직을 추가할 수 있음
            userRepository.save(user); // 변경사항을 데이터베이스에 저장
            throw new RuntimeException("저장된 리프레시 토큰과 일치하지 않습니다.");
        }
    }

    // 저장되어 있는 회원을 찾음
    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByUserEmail(oAuthInfoResponse.getEmail())
                .map(User::getUserId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    // oauth 통해 가져온 사용자 통해서 회원 만들기
    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .userEmail(oAuthInfoResponse.getEmail())
                .userNickname(oAuthInfoResponse.getNickname())
                .userLoginMethod(oAuthInfoResponse.getOAuthProvider().toString())
                .build();
        return userRepository.save(user).getUserId();
    }
}
