package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_bill")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(VideoBillId.class)
public class VideoBill {

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @Id
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    private double totalAmount;

    public static VideoBill of(Video video, LocalDate today, double totalAmount) {
        return VideoBill.builder()
                .video(video)
                .createdAt(today)
                .totalAmount(totalAmount)
                .build();
    }
}
