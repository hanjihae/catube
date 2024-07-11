package com.sparta.catube.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayTimeRankDto {
    private Long videoId;
    private String videoTitle;
    private long playTime;
    private long totalPlayTime;
    private LocalDate start;
    private LocalDate end;

    public PlayTimeRankDto(Long videoId, String videoTitle, long playTime, long totalPlayTime, LocalDate start, LocalDate end) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
        this.start = start;
        this.end = end;
    }
}
