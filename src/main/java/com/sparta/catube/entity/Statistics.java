package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "statistics")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    private String stVideoTitle;
    private int stDailyViewCount;
    private long stDailyPlayTime;

    @Column(nullable = false)
    private Long stVideoId;
}
