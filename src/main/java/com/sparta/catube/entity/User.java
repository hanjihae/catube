package com.sparta.catube.entity;

import com.sparta.catube.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime userCreatedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime userUpdatedAt;

    @Column(nullable = false)
    private String userLoginMethod;

    private String userStatus;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    private String refreshToken;

    @Builder
    public User(String userEmail, String userNickname, String userLoginMethod) {
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userLoginMethod = userLoginMethod;
        this.userType = "user";
    }
}
