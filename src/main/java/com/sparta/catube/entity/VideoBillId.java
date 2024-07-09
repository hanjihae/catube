package com.sparta.catube.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class VideoBillId implements Serializable {
    private Long video;
    private LocalDate createdAt;
}
