package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class VideoAdDto {
    private Long id;
    private Long vaVideoId;
    private Long vaAdId;
    private long vaPosition;
}
