//package com.sparta.catube.service;
//
//import com.sparta.catube.dto.StatisticsDto;
//import com.sparta.catube.entity.Statistics;
//import com.sparta.catube.entity.Video;
//import com.sparta.catube.repository.StatisticsRepository;
//import com.sparta.catube.repository.VideoRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.NumberFormat;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//@Service
//@RequiredArgsConstructor
//public class StatisticsService {
//
//
//    private final StatisticsRepository statisticsRepository;
//    private final VideoRepository videoRepository;
//    private List<StatisticsDto> batchResults = new ArrayList<>();
//
//    LocalDate today = LocalDate.now();
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    public String getCurrentWeekRange() {
//        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
//        LocalDate endOfWeek = today.plusDays(7 - today.getDayOfWeek().getValue());
//
//        String startOfWeekFormatted = startOfWeek.format(formatter);
//        String endOfWeekFormatted = endOfWeek.format(formatter);
//        return startOfWeekFormatted + " - " + endOfWeekFormatted;
//    }
//
//    public String getCurrentMonthRange() {
//        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
//        LocalDate startOfMonth = yearMonth.atDay(1);
//        LocalDate endOfMonth = yearMonth.atEndOfMonth();
//
//        String startOfMonthFormatted = startOfMonth.format(formatter);
//        String endOfMonthFormatted = endOfMonth.format(formatter);
//        return startOfMonthFormatted + " - " + endOfMonthFormatted;
//    }
//
//    public String getToday() {
//        return today.format(formatter);
//    }
//
//    public String formatVideoTotalViews(int videoTotalViews) {
//        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
//        return numberFormat.format(videoTotalViews) + "회";
//    }
//
//    public String formatVideoTotalPlayTime(long videoTotalPlayTime) {
//        long hours = videoTotalPlayTime / 3600;
//        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
//        return numberFormat.format(hours) + "시간";
//    }
//
//    public List<Video> findTop5ByTotalViewsToday() {
//        return videoRepository.findTop5ByTotalViewsToday();
//    }
//
//    public List<Video> findTop5ByTotalViewsThisWeek() {
//        return videoRepository.findTop5ByTotalViewsThisWeek();
//    }
//
//    public List<Video> findTop5ByTotalViewsThisMonth() {
//        return videoRepository.findTop5ByTotalViewsThisMonth();
//    }
//
//    public StatisticsDto createStatisticsDto(Video video, String type, String period, String dateRange) {
//        String value;
//        if (type.equals("조회수")) {
//            value = formatVideoTotalViews(video.getVideoTotalViews());
//        } else {
//            value = formatVideoTotalPlayTime(video.getVideoTotalPlaytime());
//        }
//        Statistics statistics = Statistics.of(type, period, dateRange, video.getVideoTitle(), value, video.getVideoId());
//        return new StatisticsDto(statistics);
//    }
//
//    public void addBatchResult(StatisticsDto statisticsDto) {
//        batchResults.add(statisticsDto);
//    }
//
//    public List<StatisticsDto> getBatchResults() {
//        return new ArrayList<>(batchResults);
//    }
//
//    public void clearBatchResults() {
//        batchResults.clear();
//    }
//}
