package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "views")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Views extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewsId;

    private int viewsCount;
    private long viewsLastWatchedTime;
    private long viewsPlaytime;

    @ManyToOne
    @JoinColumn(name = "viewsUserId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "viewsVideoId")
    private Video video;

    public static Views createNewView(User user, Video video, Long authenticatedUserId) {
        Views view = new Views();
        view.setUser(user); // 인증된 사용자를 시청기록에 저장
        view.setVideo(video); // 조회한 동영상 시청기록에 저장
        view.setViewsCount(!video.getUser().getUserId().equals(authenticatedUserId) ? 1 : 0);
        view.setViewsLastWatchedTime(0);
        view.setViewsPlaytime(0);
        return view;
    }

}
