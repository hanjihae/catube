package com.sparta.catube.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class VideoStatId implements Serializable {
    private Long video;
    private LocalDate createdAt;
}
