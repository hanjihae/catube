package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ad_views")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdViews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adViewsId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "video_ad_id")
    private VideoAd videoAd;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    public static AdViews of(VideoAd videoAd, Ad ad) {
        return AdViews.builder()
                .createdAt(LocalDateTime.now())
                .videoAd(videoAd)
                .ad(ad)
                .build();
    }
}
