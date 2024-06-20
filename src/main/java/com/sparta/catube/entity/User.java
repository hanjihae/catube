package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    @Column(nullable = false, updatable = false)
    private Timestamp userCreatedAt;

    @Column(nullable = false)
    private Timestamp userUpdatedAt;

    @Column(nullable = false)
    private String userLoginMethod;

    private String userStatus;
}
