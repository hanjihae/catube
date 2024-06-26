package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class VideoRequestDto {
    private Long userId;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private Time videoLength;
}
