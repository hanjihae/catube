package com.sparta.catube.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ViewCountRankDto {
    private Long videoId;
    private String videoTitle;
    private int viewCount;
    private int totalViewCount;
    private LocalDate start;
    private LocalDate end;

    public ViewCountRankDto(Long videoId, String videoTitle, int viewCount, int totalViewCount, LocalDate start, LocalDate end) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.viewCount = viewCount;
        this.totalViewCount = totalViewCount;
        this.start = start;
        this.end = end;
    }
}
