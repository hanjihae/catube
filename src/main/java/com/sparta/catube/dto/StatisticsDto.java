package com.sparta.catube.dto;

import com.sparta.catube.entity.Statistics;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticsDto {
    private String stType;
    private String stPeriod;
    private String stVideoTitle;
    private String stValue;

//    public StatisticsDto(Statistics statistics) {
//        this.stType = statistics.getStType();
//        this.stPeriod = statistics.getStPeriod();
//        this.stVideoTitle = statistics.getStVideoTitle();
//        this.stValue = statistics.getStValue();
//    }
}
