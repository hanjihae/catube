package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    private String stType;
    private String stPeriod;
    private int stValue;
    private Date stPeriodStart;
    private Date stPeriodEnd;

    @Column(nullable = false, updatable = false)
    private Timestamp stCreatedAt;

    @ManyToOne
    @JoinColumn(name = "stVideoId")
    private Video video;
}
