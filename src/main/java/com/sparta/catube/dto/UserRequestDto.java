package com.sparta.catube.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private Long userId;
    private String refreshToken;
}