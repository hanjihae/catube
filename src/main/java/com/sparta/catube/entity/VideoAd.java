package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.ToOne;

import java.sql.Time;

@Entity
@Table(name = "video_ad")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaVideoId")
    private Video video;

    @OneToOne
    @JoinColumn(name = "vaAdId")
    private Ad ad;

    private long vaPosition;

    public static VideoAd createVideoAd(Video video, long vaPosition) {
        return VideoAd.builder()
                .video(video)
                .vaPosition(vaPosition)
                .build();
    }

    public void saveAd(Ad ad) {
        this.ad = ad;
    }
}
