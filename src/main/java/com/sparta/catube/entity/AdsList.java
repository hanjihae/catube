package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ads_list")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adsListId;
    private String adTitle;
}
