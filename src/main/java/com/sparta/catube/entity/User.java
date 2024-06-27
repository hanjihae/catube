package com.sparta.catube.entity;

import com.sparta.catube.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.swing.text.StyledEditorKit;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
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

    @Builder
    public User(String userEmail, String userNickname, String userLoginMethod) {
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userLoginMethod = userLoginMethod;
        this.userType = "USER";
        this.userStatus = true;
    }
}
