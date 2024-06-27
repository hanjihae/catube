package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "ad")
@Data
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    private String adUrl;
    private long adLength;
}
