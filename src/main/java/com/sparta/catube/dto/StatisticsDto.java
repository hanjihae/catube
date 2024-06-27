package com.sparta.catube.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class StatisticsDto {
    private Long stId;
    private String stType;
    private String stPeriod;
    private int stValue;
    private Date stPeriodStart;
    private Date stPeriodEnd;
    private Long stVideoId;
}
