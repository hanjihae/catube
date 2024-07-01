package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "views")
@Getter
@Builder
@AllArgsConstructor
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

    public static Views of(User user, Video video, Long authenticatedUserId) {
        return Views.builder()
                .user(user)
                .video(video)
                .viewsCount(!video.getUser().getUserId().equals(authenticatedUserId) ? 1 : 0)
                .viewsLastWatchedTime(0)
                .viewsPlaytime(0)
                .build();
    }

    public void saveViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public void saveViewsLastWatchedTime(long viewsLastWatchedTime) {
        this.viewsLastWatchedTime = viewsLastWatchedTime;
    }

    public void saveViewsPlaytime(long viewsPlaytime) {
        this.viewsPlaytime = viewsPlaytime;
    }
}
