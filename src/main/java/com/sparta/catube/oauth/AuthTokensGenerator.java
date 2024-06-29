package com.sparta.catube.oauth;

import com.sparta.catube.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 120;            // 2시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokens generate(Long memberId) {
        String accessToken = generateNewToken(memberId, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = generateNewToken(memberId, REFRESH_TOKEN_EXPIRE_TIME);
        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public String generateAccessToken(Long memberId) {
        return generateNewToken(memberId, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateNewToken(Long memberId, long expiredAt) {
        long now = (new Date()).getTime();
        Date expiredDate = new Date(now + expiredAt);
        return jwtTokenProvider.generate(memberId.toString(), expiredDate);
    }
}
