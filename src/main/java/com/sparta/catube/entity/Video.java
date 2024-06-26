package com.sparta.catube.entity;

import com.sparta.catube.dto.VideoRequestDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Time;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Video extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(nullable = false)
    private String videoTitle;

    private String videoDescription;

    @Column(nullable = false)
    private String videoUrl;

    private int videoTotalViews;
    private Time videoTotalPlaytime;
    private Time videoLength;

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
        video.setVideoTotalViews(0);    // 총 조회수
        video.setVideoTotalPlaytime(new Time(0));   // 총 재생시간
        video.setVideoDeleteCheck(false);   // 동영상 삭제여부 true: 삭제, false: 삭제안함
        video.setVideoPublicCheck(true);    // 동영상 공개여부 true: 공개, false: 비공개
        return video;
    }
}
