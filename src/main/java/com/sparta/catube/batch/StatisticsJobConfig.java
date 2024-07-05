//package com.sparta.catube.batch;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@RequiredArgsConstructor
//@Configuration
//public class StatisticsJobConfig {
//
//    @Autowired
//    private JobRepository jobRepository;
//    @Autowired
//    private StatisticsStepConfig statisticsStepConfig;
//
//    @Bean
//    public Job statisticsJob() {
//        return new JobBuilder("statisticsJob", jobRepository)
//                .start(statisticsStepConfig.statisticsStep())
//                .next(statisticsStepConfig.statisticsStep())
//                .next(statisticsStepConfig.statisticsStep())
//                .build();
//    }
//}