package com.sparta.catube.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    // HS512 알고리즘을 사용하는 SecretKey 생성
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // JwtTokenProvider 생성자, secretKey 값을 Base64로 디코딩하여 Key로 변환
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    }

    // JWT 토큰 생성
    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)  // 토큰의 subject 설정 (사용자 식별자)
                .setExpiration(expiredAt)  // 토큰 만료 시간 설정
                .signWith(secretKey)  // 서명에 사용할 키와 알고리즘 설정
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
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getClaimFromToken(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰에서 subject 파싱
    public String parseSubject(String accessToken) {
        return getClaimFromToken(accessToken, Claims::getSubject);
    }

    // 토큰이 유효하지 않으면 새로운 JWT 토큰을 생성
    public String refreshTokenIfInvalid(String token, UserDetails userDetails) {
        if (!validateToken(token, userDetails)) {
            final String username = parseSubject(token);
            Date now = new Date();
            Date expiredAt = new Date(now.getTime() + 5 * 60 * 1000); // 5분 유효
            return generate(username, expiredAt);
        }
        return token;
    }
}
