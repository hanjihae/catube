package com.sparta.catube.entity;

import com.sparta.catube.dto.AdRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Table(name = "ad")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    private String adUrl;
    private long adLength;
    private int adWatchedCount;

    public static Ad of(AdRequestDto adDto) {
        return Ad.builder()
                .adUrl(adDto.getAdUrl())
                .adLength(adDto.getAdLength())
                .adWatchedCount(0)
                .build();
    }

    public void saveAdWatchedCount(int adWatchedCount) {
        this.adWatchedCount = adWatchedCount;
    }
}
