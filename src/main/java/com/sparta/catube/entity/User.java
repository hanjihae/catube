package com.sparta.catube.entity;

import com.sparta.catube.common.BaseTimeEntity;
import com.sparta.catube.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {
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
