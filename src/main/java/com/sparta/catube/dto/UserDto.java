package com.sparta.catube.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String userType;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String userPw;
    private String userImgUrl;
    private String userLoginMethod;
    private String userStatus;
}
