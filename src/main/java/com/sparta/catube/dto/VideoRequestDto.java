package com.sparta.catube.dto;

import lombok.Data;

@Data
public class VideoRequestDto {
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private long videoLength;
}
