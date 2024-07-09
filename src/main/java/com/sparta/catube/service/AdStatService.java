package com.sparta.catube.service;

import com.sparta.catube.entity.AdStat;
import com.sparta.catube.entity.VideoAd;
import com.sparta.catube.repository.AdStatRepository;
import com.sparta.catube.repository.VideoAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdStatService {

    private final VideoAdRepository videoAdRepository;
    private final AdStatRepository adStatRepository;

    public void saveAdViewCount() {
        LocalDate today = LocalDate.now();
        List<Object[]> dailyAdList = videoAdRepository.countGroupedByVideoIdAndVideoAdId(today);
        List<AdStat> adStatList = new ArrayList<>();
        for (Object[] dailyAd : dailyAdList) {
            VideoAd videoAd = (VideoAd) dailyAd[0];
            int dailyAdCount = ((Long) dailyAd[1]).intValue();
            AdStat adStat = AdStat.of(videoAd, dailyAdCount);
            adStatList.add(adStat);
        }
        adStatRepository.saveAll(adStatList);
    }

}
