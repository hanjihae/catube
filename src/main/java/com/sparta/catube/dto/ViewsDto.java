package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class ViewsDto {
    private Long viewsId;
    private Time viewsLastWatchedTime;
    private int viewsAdWatchedCount;
    private int viewsCount;
    private long viewsPlaytime;
    private Long viewsUserId;
    private Long viewsVideoId;
}