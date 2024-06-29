package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Video extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(nullable = false)
    private String videoTitle;

    private String videoDescription;

    @Column(nullable = false)
    private String videoUrl;

    private int adWatchedCount;
    private int videoTotalViews;
    private long videoTotalPlaytime;
    private long videoLength;

    @Column(nullable = false)
    private Boolean videoDeleteCheck;

    @Column(nullable = false)
    private Boolean videoPublicCheck;

    @ManyToOne
    @JoinColumn(name = "videoUserId")
    private User user;

    public static Video of(User user, VideoRequestDto videoRequestDto) {
        Video video = new Video();
        video.setUser(user);
        video.setVideoTitle(videoRequestDto.getVideoTitle());
        video.setVideoDescription(videoRequestDto.getVideoDescription());
        video.setVideoUrl(videoRequestDto.getVideoUrl());
        video.setVideoLength(videoRequestDto.getVideoLength());
        video.setAdWatchedCount(0);
        video.setVideoTotalViews(0);    // 총 조회수
        video.setVideoTotalPlaytime(0);   // 총 재생시간 (secounds 단위)
        video.setVideoDeleteCheck(false);   // 동영상 삭제여부 true: 삭제, false: 삭제안함
        video.setVideoPublicCheck(true);    // 동영상 공개여부 true: 공개, false: 비공개
        return video;
    }

    public VideoDto toDto() {
        return new VideoDto(
                this.videoId,
                this.videoTitle,
                this.videoDescription,
                this.videoUrl,
                this.adWatchedCount,
                this.videoTotalViews,
                this.videoTotalPlaytime,
                this.videoLength,
                this.videoDeleteCheck,
                this.videoPublicCheck
        );
    }
}
