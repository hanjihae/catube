package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "views")
@Data
@NoArgsConstructor
public class Views {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewsId;

    private Time viewsLastWatchedTime;
    private int viewsAdWatchedCount;
    private int viewsCount;
    private Time viewsPlaytime;

    @ManyToOne
    @JoinColumn(name = "viewsUserId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "viewsVideoId")
    private Video video;
}
