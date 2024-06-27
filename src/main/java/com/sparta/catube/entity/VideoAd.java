package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

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

    @OneToOne
    @JoinColumn(name = "vaAdId")
    private Ad ad;

    private long vaPosition;
}
