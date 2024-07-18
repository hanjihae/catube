package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import com.sparta.catube.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userType;

    private String userName;
    private String userNickname;

    @Column(nullable = false)
    private String userEmail;

    private String userPw;
    private String userImgUrl;

    @Column(nullable = false)
    private String userLoginMethod;

    private Boolean userStatus;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    private String refreshToken;

    public void saveUserType(String userType) {
        this.userType = userType;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void saveUserPw(String userPw) {
        this.userPw = userPw;
    }

    // test
    public void saveUserId(Long userId) {
        this.userId = userId;
    }
}
