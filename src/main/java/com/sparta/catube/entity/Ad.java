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

    public static Ad createAd(AdRequestDto adDto) {
        return Ad.builder()
                .adUrl(adDto.getAdUrl())
                .adLength(adDto.getAdLength())
                .build();
    }
}
