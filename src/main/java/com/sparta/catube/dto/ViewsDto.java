package com.sparta.catube.dto;

import com.sparta.catube.entity.Views;
import lombok.Data;

import java.sql.Time;

@Data
public class ViewsDto {
    private Long viewsId;
    private int viewsCount;
    private long viewsPlaytime;
    private Long viewsUserId;
    private Long viewsVideoId;

    public ViewsDto(Views views) {
        this.viewsId = views.getViewsId();
        this.viewsCount = views.getViewsCount();
        this.viewsPlaytime = views.getViewsPlaytime();
        this.viewsUserId = views.getUser().getUserId();
        this.viewsVideoId = views.getVideo().getVideoId();
    }
}