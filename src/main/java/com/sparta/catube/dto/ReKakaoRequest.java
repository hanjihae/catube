package com.sparta.catube.dto;

import lombok.Data;

@Data
public class ReKakaoRequest {
    private Long userId;
    private String refreshToken;
}