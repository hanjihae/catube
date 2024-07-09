package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "views")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Views {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewsId;

    private long viewsLastWatchedTime;
    private long viewsPlayTime;

    @ManyToOne
    @JoinColumn(name = "viewsUserId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "viewsVideoId")
    private Video video;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Views of(User user, Video video) {
        return Views.builder()
                .user(user)
                .video(video)
                .createdAt(LocalDateTime.now())
                .viewsLastWatchedTime(0)
                .viewsPlayTime(0)
                .build();
    }

    public void saveViewsLastWatchedTime(long viewsLastWatchedTime) {
        this.viewsLastWatchedTime = viewsLastWatchedTime;
    }

    public void saveViewsPlaytime(long viewsPlayTime) {
        this.viewsPlayTime = viewsPlayTime;
    }
}
