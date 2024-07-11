package com.sparta.catube.controller;

import com.sparta.catube.dto.PlayTimeRankDto;
import com.sparta.catube.dto.ViewCountRankDto;
import com.sparta.catube.service.VideoStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video-stat")
public class VideoStatController {

    private final VideoStatService videoStatService;

//    @PostMapping("/save-daily")
//    public void saveDailyVideoStat() throws Exception {
//        videoStatService.saveDailyStat();
//        System.out.println("1일치 조회수, 재생시간 저장 완료");
//    }

    @GetMapping("/search-daily-cnt")
    public ResponseEntity<List<ViewCountRankDto>> searchDailyVideoStatCnt() throws Exception {
        List<ViewCountRankDto> dailyViewCountRank = videoStatService.searchDailyViewCount();
        return ResponseEntity.status(HttpStatus.OK).body(dailyViewCountRank);
    }

    @GetMapping("/search-daily-time")
    public ResponseEntity<List<PlayTimeRankDto>> searchDailyVideoStatTime() throws Exception {
        List<PlayTimeRankDto> dailyPlayTimeRank = videoStatService.searchDailyPlayTime();
        return ResponseEntity.status(HttpStatus.OK).body(dailyPlayTimeRank);
    }

    @GetMapping("/search-weekly-cnt")
    public ResponseEntity<List<ViewCountRankDto>> searchWeeklyVideoStatCnt() throws Exception {
        List<ViewCountRankDto> weeklyViewCountRank = videoStatService.searchWeeklyViewCount();
        return ResponseEntity.status(HttpStatus.OK).body(weeklyViewCountRank);
    }

    @GetMapping("/search-weekly-time")
    public ResponseEntity<List<PlayTimeRankDto>> searchWeeklyVideoStatTime() throws Exception {
        List<PlayTimeRankDto> weeklyPlayTimeRank = videoStatService.searchWeeklyPlayTime();
        return ResponseEntity.status(HttpStatus.OK).body(weeklyPlayTimeRank);
    }

    @GetMapping("/search-monthly-cnt")
    public ResponseEntity<List<ViewCountRankDto>> searchMonthlyVideoStatCnt() throws Exception {
        List<ViewCountRankDto> monthlyViewCountRank = videoStatService.searchMonthlyViewCount();
        return ResponseEntity.status(HttpStatus.OK).body(monthlyViewCountRank);
    }

    @GetMapping("/search-monthly-time")
    public ResponseEntity<List<PlayTimeRankDto>> searchMonthlyVideoStatTime() throws Exception {
        List<PlayTimeRankDto> monthlyPlayTimeRank = videoStatService.searchMonthlyPlayTime();
        return ResponseEntity.status(HttpStatus.OK).body(monthlyPlayTimeRank);
    }
}
