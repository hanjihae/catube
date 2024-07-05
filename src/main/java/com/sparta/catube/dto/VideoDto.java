package com.sparta.catube.dto;

import com.sparta.catube.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class VideoDto {
    private Long videoId;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private int videoTotalViews;
    private long videoTotalPlaytime;
    private long videoLength;
    private Boolean videoDeleteCheck;
    private Boolean videoPublicCheck;

    public VideoDto(Video video) {
        this.videoId = video.getVideoId();
        this.videoTitle = video.getVideoTitle();
        this.videoDescription = video.getVideoDescription();
        this.videoUrl = video.getVideoUrl();
        this.videoTotalViews = video.getVideoTotalViews();
        this.videoTotalPlaytime = video.getVideoTotalPlaytime();
        this.videoLength = video.getVideoLength();
        this.videoDeleteCheck = video.getVideoDeleteCheck();
        this.videoPublicCheck = video.getVideoPublicCheck();
    }
}
