package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "statistics")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Long stVideoId;
}
