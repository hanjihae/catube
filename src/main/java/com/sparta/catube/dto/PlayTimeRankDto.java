package com.sparta.catube.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayTimeRankDto {
    private Long videoId;
    private String videoTitle;
    private long playTime;
    private LocalDate start;
    private LocalDate end;

    public PlayTimeRankDto(Long videoId, String videoTitle, long playTime, LocalDate start, LocalDate end) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playTime = playTime;
        this.start = start;
        this.end = end;
    }
}
