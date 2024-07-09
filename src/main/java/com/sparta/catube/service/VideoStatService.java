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

    private User getAuthenticatedUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long authenticatedUserId = Long.parseLong(userDetails.getUsername());
        return userRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new Exception("ID를 찾을 수 없습니다."));
    }

    // 1일치 조회수, 재생시간 저장
    public void saveDailyStat() throws Exception {
//        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();

//        List<Object[]> dailyStats = viewsRepository.countViewsByVideoExcludingUserGroupByVideo(user, today);
        List<Object[]> dailyStats = viewsRepository.countViewsByVideoExcludingUserGroupByVideo(today);
        List<VideoStat> vsToSave = new ArrayList<>();
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            int viewCount = video.getVideoTotalViews() + ((Number)vs[1]).intValue();
            long playTime = video.getVideoTotalPlaytime() + (long)vs[2];
            VideoStat videoStat = VideoStat.of(video, viewCount, playTime);
            vsToSave.add(videoStat);
        }
        videoStatRepository.saveAll(vsToSave);
    }

    // 1일치 조회수 조회
    public List<ViewCountRankDto> searchDailyViewCount() throws Exception {
        LocalDate today = LocalDate.now();
        List<ViewCountRankDto> dailyStatList = new ArrayList<>();
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoViewCount(today);
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            dailyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, today, today));
        }
        return dailyStatList;
    }

    // 1일치 재생시간 조회
    public List<PlayTimeRankDto> searchDailyPlayTime() throws Exception {
        LocalDate today = LocalDate.now();
        List<PlayTimeRankDto> dailyStatList = new ArrayList<>();
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoPlayTime(today);
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            dailyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, today, today));
        }
        return dailyStatList;
    }

    // 1주일치 조회수 조회
    public List<ViewCountRankDto> searchWeeklyViewCount() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<ViewCountRankDto> weeklyStatList = new ArrayList<>();
        List<Object[]> weeklyStats = videoStatRepository.findVideoViewCount(startOfWeek, endOfWeek);
        for (Object[] vs : weeklyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            weeklyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, startOfWeek, endOfWeek));
        }
        return weeklyStatList;
    }

    // 1주일치 재생시간 조회
    public List<PlayTimeRankDto> searchWeeklyPlayTime() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<PlayTimeRankDto> weeklyStatList = new ArrayList<>();
        List<Object[]> weeklyStats = videoStatRepository.findVideoPlayTime(startOfWeek, endOfWeek);
        for (Object[] vs : weeklyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            weeklyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, startOfWeek, endOfWeek));
        }
        return weeklyStatList;
    }

    // 1달치 조회수 조회
    public List<ViewCountRankDto> searchMonthlyViewCount() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        List<ViewCountRankDto> monthlyStatList = new ArrayList<>();
        List<Object[]> monthlyStats = videoStatRepository.findVideoViewCount(startOfMonth, endOfMonth);
        for (Object[] vs : monthlyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            monthlyStatList.add(new ViewCountRankDto(video.getVideoId(), video.getVideoTitle(), viewCount, startOfMonth, endOfMonth));
        }
        return monthlyStatList;
    }

    // 1달치 조회수 조회
    public List<PlayTimeRankDto> searchMonthlyPlayTime() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        List<PlayTimeRankDto> monthlyStatList = new ArrayList<>();
        List<Object[]> monthlyStats = videoStatRepository.findVideoPlayTime(startOfMonth, endOfMonth);
        for (Object[] vs : monthlyStats) {
            Video video = (Video)vs[0];
            long playTime = (long)vs[1];
            monthlyStatList.add(new PlayTimeRankDto(video.getVideoId(), video.getVideoTitle(), playTime, startOfMonth, endOfMonth));
        }
        return monthlyStatList;
    }

}
