package com.sparta.catube.dto;

import lombok.Data;

@Data
public class DailyBillDto {
    private Long videoId;
    private double totalAmount;
    private double videoAmount;
    private double adAmount;

    public DailyBillDto(Long videoId, double totalAmount, double videoAmount, double adAmount) {
        this.videoId = videoId;
        this.totalAmount = totalAmount;
        this.videoAmount = videoAmount;
        this.adAmount = adAmount;
    }
}
