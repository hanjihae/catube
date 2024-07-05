package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "adId")
    private Ad ad;

    public static VideoAd of (Video video, Ad ad) {
        return VideoAd.builder()
                .video(video)
                .ad(ad)
                .build();
    }
}
