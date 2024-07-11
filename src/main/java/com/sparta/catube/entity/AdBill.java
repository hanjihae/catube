package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ad_bill")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_ad_id")
    private VideoAd videoAd;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    private double totalAmount;

    public static AdBill of(VideoAd videoAd, LocalDate today, double totalAmount) {
        return AdBill.builder()
                .videoAd(videoAd)
                .createdAt(today)
                .totalAmount(totalAmount)
                .build();
    }
}
