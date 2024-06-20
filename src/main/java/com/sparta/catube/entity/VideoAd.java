package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "video_ad")
@Data
@NoArgsConstructor
public class VideoAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaVideoId")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "vaAdId")
    private Ad ad;

    private Time vaPosition;
}
