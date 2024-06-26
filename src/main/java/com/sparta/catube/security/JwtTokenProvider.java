package com.sparta.catube.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKeyBase64) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String generate(String subject, Date expiredAt) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)  // 토큰의 subject 설정 (사용자 식별자)
                .setIssuedAt(now)      // 토큰 생성 시간 설정
                .setExpiration(expiredAt)  // 토큰 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS512)  // 서명에 사용할 키와 알고리즘 설정
                .compact();  // 토큰 생성
    }

    // 토큰에서 만료 날짜 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 토큰에서 특정 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 추출
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    // 토큰이 만료되었는지 확인
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 토큰이 유효한지 확인
    public Boolean validateToken(String token, String subject) {
        String tokenSubject = getClaimFromToken(token, Claims::getSubject);
        return (tokenSubject != null && tokenSubject.equals(subject) && !isTokenExpired(token));
    }

    // 토큰에서 subject 파싱
    public String parseSubject(String accessToken) {
        return getClaimFromToken(accessToken, Claims::getSubject);
    }
}
