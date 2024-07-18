package com.sparta.catube;

import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class GenerateTest {
    @Autowired
    private ViewsRepository viewsRepository;

    @Autowired
    private AdViewsRepository adViewsRepository;

    @Autowired
    private AdRepository adsRepository;

    @Autowired
    private VideoRepository videosRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoAdRepository videoAdRepository;

    public void generateData(int numRecords) {
        Random random = new Random();
        User user = userRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Video> videos = videosRepository.findAll();
        List<Ad> ads = adsRepository.findAll();
        List<VideoAd> videoAds = videoAdRepository.findAll();

        for (Video video : videos) {
            for (int i = 0; i < numRecords; i++) {
                int playTime = random.nextInt(2000) + 300;  // 300초에서 1800초 사이의 랜덤 시간
                Views views = Views.builder()
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .viewsLastWatchedTime(playTime)
                        .user(user)
                        .video(video)
                        .build();
                viewsRepository.save(views);

                int numAds = playTime / 300;

                for (int j = 0; j < numAds; j++) {
                    VideoAd videoAd = videoAds.get(random.nextInt(videoAds.size()));
                    AdViews adView = AdViews.builder()
                                    .createdAt(LocalDateTime.now().minusDays(1))
                                    .videoAd(videoAd).build();
                    adViewsRepository.save(adView);
                }
            }
        }
    }

    @Test
    public void testGenerateData() {
        generateData(10000);  // 10개의 기록을 생성
    }
}