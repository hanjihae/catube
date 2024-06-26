package com.sparta.catube.entity;

import com.sparta.catube.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.sql.Time;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Video extends BaseTimeEntity {
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
}
