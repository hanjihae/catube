package com.sparta.catube.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobLauncher {

    @Autowired
    private Job statisticsJob;
    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void launchJob() throws Exception {
        jobLauncher.run(statisticsJob, new JobParametersBuilder().toJobParameters());
    }
}
