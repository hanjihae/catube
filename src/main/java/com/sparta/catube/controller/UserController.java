package com.sparta.catube.controller;

import com.sparta.catube.oauth.AuthTokens;
import com.sparta.catube.oauth.KakaoLoginParams;
import com.sparta.catube.service.OAuthLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class UserController {
    @Autowired
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/re-kakao")
    public String loginAgain(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return oAuthLoginService.regenerateAccessToken(userId, req.getHeader("Authorization"));
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal UserDetails userDetails) {
        oAuthLoginService.logout(Long.parseLong(userDetails.getUsername()));
        return "로그아웃하셨습니다.";
    }
}
