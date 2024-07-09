package com.sparta.catube.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillInfoDto {
    private Long userId;
    private double totalAmount;
    private LocalDate start;
    private LocalDate end;

    public BillInfoDto(Long userId, double totalAmount, LocalDate start, LocalDate end) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.start = start;
        this.end = end;
    }
}
