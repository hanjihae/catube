package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_stat")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(VideoStatId.class)
public class VideoStat {

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @Id
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    private int dailyViewCount;
    private long dailyPlayTime;

    public static VideoStat of(Video video, LocalDate today, int dailyViewCount, long dailyPlayTime) {
        return VideoStat.builder()
                .video(video)
                .createdAt(today)
                .dailyViewCount(dailyViewCount)
                .dailyPlayTime(dailyPlayTime)
                .build();
    }
}
