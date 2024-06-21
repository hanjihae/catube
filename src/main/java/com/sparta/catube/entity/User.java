package com.sparta.catube.entity;

import com.sparta.catube.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
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
}
