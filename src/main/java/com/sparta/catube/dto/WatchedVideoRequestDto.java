package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class WatchedVideoRequestDto {
    private Long videoId;
    private Long userId;
    private long lastWatchedTime;
}
