package com.sparta.catube.batch;

import com.sparta.catube.dto.StatisticsDto;
import com.sparta.catube.entity.Video;
import com.sparta.catube.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StatisticsStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StatisticsService statisticsService;

    @Bean
    public Step statisticsStep() {
        return new StepBuilder("statisticsStep", jobRepository)
                .<Video, StatisticsDto>chunk(5, platformTransactionManager)
                .reader(itemReader(null))
                .processor(itemProcessor(null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Video> itemReader(@Value("#{jobParameters['period']}") String period) {
        return new ItemReader<Video>() {
            private List<Video> videos;
            private int nextIndex = 0;

            {
                initialize();
            }

            private void initialize() {
                try {
                    switch (period) {
                        case "TODAY":
                            videos = statisticsService.findTop5ByTotalViewsToday();
                            break;
                        case "WEEK":
                            videos = statisticsService.findTop5ByTotalViewsThisWeek();
                            break;
                        case "MONTH":
                            videos = statisticsService.findTop5ByTotalViewsThisMonth();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Video read() throws Exception {
                if (videos != null && nextIndex < videos.size()) {
                    return videos.get(nextIndex++);
                } else {
                    return null;
                }
            }
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<Video, StatisticsDto> itemProcessor(@Value("#{jobParameters['period']}") String period) {
        return video -> {
            StatisticsDto statisticsDto;
            switch (period) {
                case "TODAY":
                    statisticsDto = statisticsService.createStatisticsDto(video, "조회수", "TODAY", statisticsService.getToday());
                    break;
                case "WEEK":
                    statisticsDto = statisticsService.createStatisticsDto(video, "조회수", "WEEK", statisticsService.getCurrentWeekRange());
                    break;
                case "MONTH":
                    statisticsDto = statisticsService.createStatisticsDto(video, "조회수", "MONTH", statisticsService.getCurrentMonthRange());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown period: " + period);
            }
            return statisticsDto;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<StatisticsDto> itemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
