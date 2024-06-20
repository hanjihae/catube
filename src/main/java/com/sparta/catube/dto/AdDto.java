package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class AdDto {
    private Long adId;
    private String adUrl;
    private Time adLength;
}
