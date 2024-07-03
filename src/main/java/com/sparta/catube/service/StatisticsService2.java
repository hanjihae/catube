package com.sparta.catube.service;

import com.sparta.catube.dto.StatisticsDto;
import com.sparta.catube.entity.Statistics;
import com.sparta.catube.entity.Video;
import com.sparta.catube.repository.StatisticsRepository;
import com.sparta.catube.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class StatisticsService2 {

    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private VideoRepository videoRepository;

    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String getCurrentWeekRange() {
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = today.plusDays(7-today.getDayOfWeek().getValue());

        String startOfWeekFormatted = startOfWeek.format(formatter);
        String endOfWeekFormatted = endOfWeek.format(formatter);
        return startOfWeekFormatted + " - " + endOfWeekFormatted;
    }

    public String getCurrentMonthRange() {
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        String startOfMonthFormatted = startOfMonth.format(formatter);
        String endOfMonthFormatted = endOfMonth.format(formatter);
        return startOfMonthFormatted + " - " + endOfMonthFormatted;
    }

    public String formatVideoTotalViews(int videoTotalViews) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(videoTotalViews)+"회";
    }

    public String formatVideoTotalPlayTime(long videoTotalPlayTime) {
        long hours = videoTotalPlayTime / 3600;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(hours)+"시간";
    }

    // 1일 조회수 TOP5
    public List<StatisticsDto> videoTotalViewsTop5OfToday() throws Exception {
        List<Video> videosOfToday = videoRepository.findTop5ByTotalViewsToday();
        String getToday = today.format(formatter);
        if (videosOfToday.isEmpty()) {
            throw new Exception("오늘 업로드된 동영상이 없습니다.");
        }
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfToday.size(); i++) {
            Video video = videosOfToday.get(i);
            Statistics statisticsOfToday = Statistics.of(
                    "조회수",
                    "TODAY",
                    getToday,
                    video.getVideoTitle(),
                    formatVideoTotalViews(video.getVideoTotalViews()),
                    video.getVideoId()
            );
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfToday);
            statisticsDtos.add(statisticsDto);
        };
        return statisticsDtos;
    }

    // 1주일 조회수 TOP5
    public List<StatisticsDto> videoTotalViewsTop5OfWeek() throws Exception {
        List<Video> videosOfWeek = videoRepository.findTop5ByTotalViewsThisWeek();
        if (videosOfWeek.isEmpty()) {
            throw new Exception("이번 주 업로드된 동영상이 없습니다.");
        }
        List<Statistics> listForSave = new ArrayList<>();
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfWeek.size(); i++) {
            Video video = videosOfWeek.get(i);
            Statistics statisticsOfWeek = Statistics.of(
                    "조회수",
                    "WEEK",
                    getCurrentWeekRange(),
                    video.getVideoTitle(),
                    formatVideoTotalViews(video.getVideoTotalViews()),
                    video.getVideoId()
            );
            listForSave.add(statisticsOfWeek);
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfWeek);
            statisticsDtos.add(statisticsDto);
        }
        statisticsRepository.saveAll(listForSave);
        return statisticsDtos;
    }

    // 1달 조회수 TOP5
    public List<StatisticsDto> videoTotalViewsTop5OfMonth() throws Exception {
        List<Video> videosOfMonth = videoRepository.findTop5ByTotalViewsThisMonth();
        if (videosOfMonth.isEmpty()) {
            throw new Exception("이번 달 업로드된 동영상이 없습니다.");
        }
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfMonth.size(); i++) {
            Video video = videosOfMonth.get(i);
            Statistics statisticsOfMonth = Statistics.of(
                    "조회수",
                    "MONTH",
                    getCurrentMonthRange(),
                    video.getVideoTitle(),
                    formatVideoTotalViews(video.getVideoTotalViews()),
                    video.getVideoId()
            );
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfMonth);
            statisticsDtos.add(statisticsDto);
        }
        return statisticsDtos;
    }

    public List<StatisticsDto> videoTotalPlaytimeTop5OfToday() throws Exception {
        // 1일 재생시간 TOP5
        List<Video> videosOfToday = videoRepository.findTop5ByTotalPlayTimeToday();
        String getToday = LocalDate.now().format(formatter);
        if (videosOfToday.isEmpty()) {
            throw new Exception("오늘 업로드된 동영상이 없습니다.");
        }
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfToday.size(); i++) {
            Video video = videosOfToday.get(i);
            Statistics statisticsOfToday = Statistics.of(
                    "재생시간",
                    "TODAY",
                    getToday,
                    video.getVideoTitle(),
                    formatVideoTotalPlayTime(video.getVideoTotalPlaytime()),
                    video.getVideoId()
            );
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfToday);
            statisticsDtos.add(statisticsDto);
        }
        return statisticsDtos;
    }

    // 1주일 조회수 TOP5
    public List<StatisticsDto> videoTotalPlayTimeTop5OfWeek() throws Exception {
        List<Video> videosOfWeek = videoRepository.findTop5ByTotalPlayTimeThisWeek();
        if (videosOfWeek.isEmpty()) {
            throw new Exception("이번 주 업로드된 동영상이 없습니다.");
        }
        List<Statistics> listForSave = new ArrayList<>();
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfWeek.size(); i++) {
            Video video = videosOfWeek.get(i);
            Statistics statisticsOfWeek = Statistics.of(
                    "재생시간",
                    "WEEK",
                    getCurrentWeekRange(),
                    video.getVideoTitle(),
                    formatVideoTotalPlayTime(video.getVideoTotalPlaytime()),
                    video.getVideoId()
            );
            listForSave.add(statisticsOfWeek);
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfWeek);
            statisticsDtos.add(statisticsDto);
        }
        statisticsRepository.saveAll(listForSave);
        return statisticsDtos;
    }

    // 1달 조회수 TOP5
    public List<StatisticsDto> videoTotalPlayTimeTop5OfMonth() throws Exception {
        List<Video> videosOfMonth = videoRepository.findTop5ByTotalPlayTimeThisMonth();
        if (videosOfMonth.isEmpty()) {
            throw new Exception("이번 달 업로드된 동영상이 없습니다.");
        }
        List<StatisticsDto> statisticsDtos = new ArrayList<>();
        for (int i = 0; i < videosOfMonth.size(); i++) {
            Video video = videosOfMonth.get(i);
            Statistics statisticsOfMonth = Statistics.of(
                    "재생시간",
                    "MONTH",
                    getCurrentMonthRange(),
                    video.getVideoTitle(),
                    formatVideoTotalPlayTime(video.getVideoTotalPlaytime()),
                    video.getVideoId()
            );
            StatisticsDto statisticsDto = new StatisticsDto(statisticsOfMonth);
            statisticsDtos.add(statisticsDto);
        }
        return statisticsDtos;
    }

}