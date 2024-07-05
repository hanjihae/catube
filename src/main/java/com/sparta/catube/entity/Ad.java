package com.sparta.catube.entity;

import com.sparta.catube.dto.AdRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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

    private long vaPosition;
    private int adWatchedCount;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "adsListId")
    private AdsList adsList;

    @OneToOne(mappedBy = "ad")
    private VideoAd videoAd;

    public static Ad of(AdsList adsList, long vaPosition) {
        return Ad.builder()
                .adsList(adsList)
                .vaPosition(vaPosition)
                .adWatchedCount(0)
                .build();
    }

    public void saveVideoAd(VideoAd videoAd) {
        this.videoAd = videoAd;
    }

    public void saveAdWatchedCount(int adWatchedCount) {
        this.adWatchedCount = adWatchedCount;
    }
}
