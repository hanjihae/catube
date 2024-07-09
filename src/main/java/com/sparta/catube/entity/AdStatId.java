package com.sparta.catube.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdStatId implements Serializable {
    private Long videoAd;
    private LocalDate createdAt;
}
