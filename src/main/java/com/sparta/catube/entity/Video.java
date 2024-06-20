package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.sql.Time;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(nullable = false)
    private String videoTitle;

    private String videoDescription;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false, updatable = false)
    private Timestamp videoUploadedAt;

    @Column(nullable = false)
    private Timestamp videoUpdatedAt;

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
}
