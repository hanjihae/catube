package com.sparta.catube.controller;

import com.sparta.catube.dto.StatisticsDto;
import com.sparta.catube.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class StatisticsController {

    @Autowired
    private final StatisticsService statisticsService;

    @PostMapping("/top-views")
    public ResponseEntity<List<StatisticsDto>> viewsTop5() throws Exception {
        List<StatisticsDto> stToday = statisticsService.videoTotalViewsTop5OfToday();
        List<StatisticsDto> stWeek = statisticsService.videoTotalViewsTop5OfWeek();
        List<StatisticsDto> stMonth = statisticsService.videoTotalViewsTop5OfMonth();

        List<StatisticsDto> combinedList = Stream.concat(stToday.stream(),
                        Stream.concat(stWeek.stream(), stMonth.stream()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(combinedList);
    }

    @PostMapping("/top-playTime")
    public ResponseEntity<List<StatisticsDto>> playTimeTop5() throws Exception {
        List<StatisticsDto> stToday = statisticsService.videoTotalPlaytimeTop5OfToday();
        List<StatisticsDto> stWeek = statisticsService.videoTotalPlayTimeTop5OfWeek();
        List<StatisticsDto> stMonth = statisticsService.videoTotalPlayTimeTop5OfMonth();

        List<StatisticsDto> combinedList = Stream.concat(stToday.stream(),
                        Stream.concat(stWeek.stream(), stMonth.stream()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(combinedList);
    }
}
