package com.sparta.catube.controller;

import com.sparta.catube.dto.StatisticsDto;
import com.sparta.catube.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final JobLauncher jobLauncher;
    private final Job statisticsJob;

    @PostMapping("/top-views")
    public ResponseEntity<List<StatisticsDto>> viewsTop5() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("period", "TODAY")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        jobParameters = new JobParametersBuilder()
                .addString("period", "WEEK")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        jobParameters = new JobParametersBuilder()
                .addString("period", "MONTH")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        List<StatisticsDto> combinedList = statisticsService.getBatchResults();
        statisticsService.clearBatchResults();
        return ResponseEntity.status(HttpStatus.OK).body(combinedList);
    }

    @PostMapping("/top-playTime")
    public ResponseEntity<List<StatisticsDto>> playTimeTop5() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("period", "TODAY")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        jobParameters = new JobParametersBuilder()
                .addString("period", "WEEK")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        jobParameters = new JobParametersBuilder()
                .addString("period", "MONTH")
                .toJobParameters();
        jobLauncher.run(statisticsJob, jobParameters);

        List<StatisticsDto> combinedList = statisticsService.getBatchResults();
        statisticsService.clearBatchResults();
        return ResponseEntity.status(HttpStatus.OK).body(combinedList);
    }
}
