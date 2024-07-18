package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "video")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private int videoTotalViews;
    private long videoTotalPlaytime;
    private long videoLength;

    @Column(nullable = false)
    private Boolean videoDeleteCheck;

    @Column(nullable = false)
    private Boolean videoPublicCheck;

    private Boolean billOrNot;

    @ManyToOne
    @JoinColumn(name = "videoUserId")
    private User user;

    @OneToMany(mappedBy = "video")
    private List<VideoBill> videoBills;

    @OneToMany(mappedBy = "video")
    private List<VideoStat> videoStats;

    public static Video of(User user, VideoRequestDto videoRequestDto) {
        return Video.builder()
                .user(user)
                .videoTitle(videoRequestDto.getVideoTitle())
                .videoDescription(videoRequestDto.getVideoDescription())
                .videoUrl(videoRequestDto.getVideoUrl())
                .videoLength(videoRequestDto.getVideoLength())
                .videoTotalViews(0) // 총 조회수
                .videoTotalPlaytime(0)  // 총 재생시간 (secounds 단위)
                .videoDeleteCheck(false)    // 동영상 삭제여부 true: 삭제, false: 삭제안함
                .videoPublicCheck(true)     // 동영상 공개여부 true: 공개, false: 비공개
                .billOrNot(false) // 정산 여부
                .build();
    }

    public void update(VideoRequestDto videoRequestDto) {
        this.videoTitle = videoRequestDto.getVideoTitle();
        this.videoDescription = videoRequestDto.getVideoDescription();
        this.videoUrl = videoRequestDto.getVideoUrl();
        this.videoLength = videoRequestDto.getVideoLength();
    }

    public void saveVideoTotalViews(int videoTotalViews) {
        this.videoTotalViews = videoTotalViews;
    }

    public void saveVideoTotalPlaytime(long videoTotalPlaytime) {
        this.videoTotalPlaytime = videoTotalPlaytime;
    }

    public void saveBillOrNot(boolean billOrNot) {
        this.billOrNot = billOrNot;
    }

    // test
    public void saveVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void saveVideoLength(long videoLength) {
        this.videoLength = videoLength;
    }

}
