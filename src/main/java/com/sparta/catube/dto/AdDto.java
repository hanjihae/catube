package com.sparta.catube.dto;

import com.sparta.catube.entity.Ad;
import lombok.Data;

import java.sql.Time;

@Data
public class AdDto {
    private Long adId;
    private String adUrl;
    private long adLength;
    private int adWatchedCount;

    public AdDto(Ad ad) {
        this.adId = ad.getAdId();
        this.adUrl = ad.getAdUrl();
        this.adLength = ad.getAdLength();
        this.adWatchedCount = ad.getAdWatchedCount();
    }
}
