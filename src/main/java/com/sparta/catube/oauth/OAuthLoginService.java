package com.sparta.catube.oauth;

import com.sparta.catube.entity.User;
import com.sparta.catube.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        userRepository.updateUserRefreshToken(userId, refreshToken);

        return authTokens;
    }

    @Transactional
    public String regenerateAccessToken(Long userId, String providedRefreshToken) {
        User user = userRepository.findByUserId(userId).orElseThrow();
        String refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            throw new RuntimeException("저장된 리프레시 토큰이 존재하지 않습니다.");
        }
        if (providedRefreshToken.equals(refreshToken)) {
            // 리프레시 토큰이 일치하면 새로운 Access 토큰 발급
            return authTokensGenerator.generateAccessToken(user.getUserId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 리프리세 토큰이 아닙니다.");
        }
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 리프레시 토큰을 삭제하거나 만료 처리를 수행
        user.saveRefreshToken(null); // 예시로 간단히 삭제 처리
        userRepository.save(user); // 변경사항을 데이터베이스에 저장
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
                .userType("USER")
                .userLoginMethod(oAuthInfoResponse.getOAuthProvider().toString())
                .build();
        return userRepository.save(user).getUserId();
    }
}
