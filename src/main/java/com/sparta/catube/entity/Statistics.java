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
public class Statistics extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    private String stSubject; // 조회수/재생시간 주제
    private String stType;  // 1일, 1주일, 1달
    private String stPeriod;    // 기간
    private String stVideoTitle;    // 동영상 제목
    private String stValue;    // 조회수/재생시간 값

    @Column(nullable = false)
    private Long stVideoId;

    public static Statistics of(String stSubject, String stType, String stPeriod, String stVideoTitle, String stValue, Long videoId) {
        return Statistics.builder()
                .stSubject(stSubject)
                .stType(stType)
                .stPeriod(stPeriod)
                .stVideoTitle(stVideoTitle)
                .stValue(stValue)
                .stVideoId(videoId)
                .build();
    }
}
