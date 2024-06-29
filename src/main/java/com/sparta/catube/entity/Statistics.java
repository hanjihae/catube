package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Statistics extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    private String stType;
    private String stPeriod;
    private int stValue;
    private Date stPeriodStart;
    private Date stPeriodEnd;

    @ManyToOne
    @JoinColumn(name = "stVideoId")
    private Video video;
}
