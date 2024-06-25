package com.sparta.catube.oauth;

import com.sparta.catube.entity.User;
import com.sparta.catube.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final KakaoApiClient kakaoApiClient; // 추가

    public AuthTokens login(OAuthLoginParams params) {
        // Kakao에서 사용자 정보 요청    == 액세스 토큰 가져옴
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        System.out.println(oAuthInfoResponse);
        // Kakao에서 받은 사용자 정보를 이용하여 회원 조회 및 생성
        Long userId = findOrCreateUser(oAuthInfoResponse);

        // AccessToken 생성
        return authTokensGenerator.generate(userId);
    }


    // 저장되어 있는 회원을 찾음
    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        System.out.println("닉네임" + oAuthInfoResponse.getNickname());
        System.out.println("로그인방법" + oAuthInfoResponse.getOAuthProvider());
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
        System.out.println("닉네임" + oAuthInfoResponse.getNickname());
        System.out.println("로그인방법" + oAuthInfoResponse.getOAuthProvider());
        return userRepository.save(user).getUserId();
    }
}
