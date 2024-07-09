package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "video_ad")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoAdId;

    @ManyToOne
    @JoinColumn(name = "vaVideoId")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "adId")
    private Ad ad;

    private int adWatchedCount;
    private boolean billOrNot;

    @OneToMany(mappedBy = "videoAd")
    private List<AdViews> adViews;

    public static VideoAd of (Video video, Ad ad) {
        return VideoAd.builder()
                .video(video)
                .ad(ad)
                .adWatchedCount(0)  // 광고의 총 누적조회수
                .billOrNot(false)
                .build();
    }

    public void saveAdWatchedCount(int adWatchedCount) {
        this.adWatchedCount = adWatchedCount;
    }
}
