package com.sparta.catube.service;

import com.sparta.catube.dto.PlayTimeRankDto;
import com.sparta.catube.dto.ViewCountRankDto;
import com.sparta.catube.entity.User;
import com.sparta.catube.entity.Video;
import com.sparta.catube.entity.VideoStat;
import com.sparta.catube.repository.UserRepository;
import com.sparta.catube.repository.VideoRepository;
import com.sparta.catube.repository.VideoStatRepository;
import com.sparta.catube.repository.ViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoStatService {

    public final UserRepository userRepository;
    public final VideoRepository videoRepository;
    public final ViewsRepository viewsRepository;
    public final VideoStatRepository videoStatRepository;

    public LocalDate today = LocalDate.now().minusDays(1);

    // 1일치 조회수, 재생시간 저장
//    public void saveDailyStat() throws Exception {
//        List<Object[]> dailyStats = viewsRepository.countViewsByVideoExcludingUserGroupByVideo(today);
//        List<VideoStat> vsToSave = new ArrayList<>();
//        List<Video> videoToSave = new ArrayList<>();
//        for (Object[] vs : dailyStats) {
//            Video video = (Video)vs[0];
//            int viewCount = ((Number)vs[1]).intValue();
//            long playTime = (long)vs[2];
//            VideoStat videoStat = VideoStat.of(video, today, viewCount, playTime);
//            vsToSave.add(videoStat);
//            video.saveVideoTotalPlaytime(video.getVideoTotalPlaytime() + playTime);
//            videoToSave.add(video);
//        }
//        videoStatRepository.saveAll(vsToSave);
//        videoRepository.saveAll(videoToSave);
//    }

    // 1일치 조회수 조회
    public List<ViewCountRankDto> searchDailyViewCount() throws Exception {
        List<ViewCountRankDto> dailyStatList = new ArrayList<>();
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoViewCount(today);
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            int totalViewCount = video.getVideoTotalViews() + viewCount;
            dailyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, totalViewCount, today, today));
        }
        return dailyStatList;
    }

    // 1일치 재생시간 조회
    public List<PlayTimeRankDto> searchDailyPlayTime() throws Exception {
        List<PlayTimeRankDto> dailyStatList = new ArrayList<>();
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoPlayTime(today);
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            long totalPlayTime = video.getVideoTotalPlaytime() + playTime;
            dailyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, totalPlayTime, today, today));
        }
        return dailyStatList;
    }

    // 1주일치 조회수 조회
    public List<ViewCountRankDto> searchWeeklyViewCount() throws Exception {
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<ViewCountRankDto> weeklyStatList = new ArrayList<>();
        List<Object[]> weeklyStats = videoStatRepository.findVideoViewCount(startOfWeek, endOfWeek);
        for (Object[] vs : weeklyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            int totalViewCount = video.getVideoTotalViews() + viewCount;
            weeklyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, totalViewCount, today, today));
        }
        return weeklyStatList;
    }

    // 1주일치 재생시간 조회
    public List<PlayTimeRankDto> searchWeeklyPlayTime() throws Exception {
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<PlayTimeRankDto> weeklyStatList = new ArrayList<>();
        List<Object[]> weeklyStats = videoStatRepository.findVideoPlayTime(startOfWeek, endOfWeek);
        for (Object[] vs : weeklyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            long totalPlayTime = video.getVideoTotalPlaytime() + playTime;
            weeklyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, totalPlayTime, startOfWeek, endOfWeek));
        }
        return weeklyStatList;
    }

    // 1달치 조회수 조회
    public List<ViewCountRankDto> searchMonthlyViewCount() throws Exception {
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        List<ViewCountRankDto> monthlyStatList = new ArrayList<>();
        List<Object[]> monthlyStats = videoStatRepository.findVideoViewCount(startOfMonth, endOfMonth);
        for (Object[] vs : monthlyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            int totalViewCount = video.getVideoTotalViews() + viewCount;
            monthlyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, totalViewCount, startOfMonth, endOfMonth));
        }
        return monthlyStatList;
    }

    // 1달치 조회수 조회
    public List<PlayTimeRankDto> searchMonthlyPlayTime() throws Exception {
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        List<PlayTimeRankDto> monthlyStatList = new ArrayList<>();
        List<Object[]> monthlyStats = videoStatRepository.findVideoPlayTime(startOfMonth, endOfMonth);
        for (Object[] vs : monthlyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            long totalPlayTime = video.getVideoTotalPlaytime() + playTime;
            monthlyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, totalPlayTime, startOfMonth, endOfMonth));
        }
        return monthlyStatList;
    }
}
