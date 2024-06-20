package com.sparta.catube.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillDto {
    private Long billId;
    private BigDecimal billVideoAmount;
    private BigDecimal billAdAmount;
    private BigDecimal billTotalAmount;
    private Long billUserId;
}
