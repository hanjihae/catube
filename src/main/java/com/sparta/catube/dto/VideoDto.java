package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class VideoDto {
    private Long videoId;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private int videoTotalViews;
    private Time videoTotalPlaytime;
    private Time videoLength;
    private Boolean videoDeleteCheck;
    private Boolean videoPublicCheck;
}
