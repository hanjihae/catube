package com.sparta.catube.dto;

import com.sparta.catube.entity.Bill;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillDto {
    private Long billId;
    private long billVideoAmount;
    private long billAdAmount;
    private long billTotalAmount;
    private Long billUserId;
    private Long videoId;

    public BillDto(long videoAmount, long adsAmount, long totalAmount, Long videoId, Long userId) {
        this.billVideoAmount = videoAmount;
        this.billAdAmount = adsAmount;
        this.billTotalAmount = totalAmount;
        this.billUserId = userId;
        this.videoId = videoId;
    }
}
