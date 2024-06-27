package com.sparta.catube.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class VideoDto {
    private Long videoId;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private int adWatchedCount;
    private int videoTotalViews;
    private long videoTotalPlaytime;
    private long videoLength;
    private Boolean videoDeleteCheck;
    private Boolean videoPublicCheck;
}
