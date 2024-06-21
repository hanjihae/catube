package com.sparta.catube.dto;

import lombok.Data;

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
