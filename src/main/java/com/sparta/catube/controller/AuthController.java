package com.sparta.catube.controller;

import com.sparta.catube.dto.UserRequestDto;
import com.sparta.catube.oauth.AuthTokens;
import com.sparta.catube.oauth.KakaoLoginParams;
import com.sparta.catube.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {
    @Autowired
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/re-kakao")
    public ResponseEntity<AuthTokens> loginAgain(@RequestBody UserRequestDto userRequestDto) {
        AuthTokens authTokens = oAuthLoginService.regenerateAccessToken(userRequestDto.getUserId(), userRequestDto.getRefreshToken());
        return ResponseEntity.ok(authTokens);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody UserRequestDto userRequestDto) {
        oAuthLoginService.logout(userRequestDto.getUserId());
        return "로그아웃하셨습니다.";
    }

}
