package com.sparta.catube.service;

import com.sparta.catube.dto.BillInfoDto;
import com.sparta.catube.dto.DailyBillDto;
import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final UserRepository userRepository;
    private final VideoStatRepository videoStatRepository;
    private final VideoRepository videoRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdStatRepository adStatRepository;
    private final VideoBillRepository videoBillRepository;
    private final AdBillRepository adBillRepository;

    private User getAuthenticatedUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long authenticatedUserId = Long.parseLong(userDetails.getUsername());
        return userRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new Exception("ID를 찾을 수 없습니다."));
    }

    public void calculateVideoAmount() throws Exception {
        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();
        List<VideoBill> vbList = new ArrayList<>();

        // 사용자의 비디오 리스트, 그 비디오들의 ID 목록 조회
        List<Video> myVideoList = videoRepository.findByUserUserId(user.getUserId());
        Set<Long> myVideoIds = myVideoList.stream()
                .map(Video::getVideoId)
                .collect(Collectors.toSet());

        // 오늘 날짜의 비디오 통계 조회
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoViewCount(today);
        for (Object[] vs : dailyStats) {
            double videoAmount = 0.0;
            Video video = (Video) vs[0];
            int viewCount = ((Number) vs[1]).intValue();
            if (myVideoIds.contains(video.getVideoId())) {
                videoAmount = calculateVideoAmount(viewCount);
            }
            VideoBill videoBill = VideoBill.of(video, videoAmount);
            vbList.add(videoBill);
        }
        videoBillRepository.saveAll(vbList);
    }

    public double calculateVideoAmount(int viewCount) {
        if (viewCount >= 100000 && viewCount < 500000) { // 조회수 10만 이상, 50만 미만
            return viewCount * 1.1;
        } else if (viewCount >= 500000 && viewCount < 1000000) { // 조회수 50만 이상, 100만 미만
            return viewCount * 1.3;
        } else if (viewCount >= 1000000) { // 조회수 100만 이상
            return viewCount * 1.5;
        } else { // 조회수 10만 이하
            return viewCount;
        }
    }

    public void calculateAdAmount() throws Exception {
        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();
        // 사용자의 동영상 리스트 가져오기
        List<Video> myVideoList = videoRepository.findByUserUserId(user.getUserId());
        // 오늘 쌓인 videoAdId별 광고 조회수 가져오기
        List<Object[]> dailyAdStats = adStatRepository.findDailyViewCount(today);
        Map<VideoAd, Integer> adViewCountMap = new HashMap<>();
        // Map<VideoAd, 오늘치 광고 조회수>
        for (Object[] vs : dailyAdStats) {
            VideoAd videoAd = (VideoAd) vs[0];
            int viewCount = ((Number) vs[1]).intValue();
            if (viewCount != 0) {
                adViewCountMap.put(videoAd, viewCount);
            }
        }
        List<AdBill> abList = new ArrayList<>();
        // 내 비디오 하나당
        for (Video video : myVideoList) {
            // 꽂힌 광고 리스트
            List<VideoAd> myAdList = videoAdRepository.findByVideo(video);
            for (VideoAd videoAd : myAdList) {
                double adAmount = 0.0;
                Integer viewCount = adViewCountMap.get(videoAd);
                if (viewCount != null) {
                    if (viewCount >= 100000 && viewCount < 500000) {
                        adAmount += viewCount * 12;
                    } else if (viewCount >= 500000 && viewCount < 1000000) {
                        adAmount += viewCount * 15;
                    } else if (viewCount >= 1000000) {
                        adAmount += viewCount * 20;
                    } else {
                        adAmount += viewCount * 10;
                    }
                }
                AdBill ab = AdBill.of(videoAd, adAmount);
                abList.add(ab);
            }
        }
        adBillRepository.saveAll(abList);
    }

    public List<Object> searchDailyTotalAmount() throws Exception {
        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();

        List<Video> myVideoList = videoRepository.findByUserUserId(user.getUserId());
        List<DailyBillDto> dailyBillDtoList = new ArrayList<>();
        double overallTotalAmount = 0.0;

        for (Video video : myVideoList) {
            double videoAmount = 0.0;
            double adAmount = 0.0;
            double totalAmount = 0.0;
            // 오늘의 동영상 정상비 가져오기
            List<VideoBill> videoBillList = videoBillRepository.findByVideoAndCreatedAt(video, today);
            videoAmount = videoBillList.stream().mapToDouble(VideoBill::getTotalAmount).sum();
            // 비디오에 해당하는 모든 비디오 광고를 가져오고, 각 광고에 대한 오늘의 광고 정산비 가져오기
            List<VideoAd> videoAdList = videoAdRepository.findByVideo(video);
            for (VideoAd videoAd : videoAdList) {
                List<AdBill> adBillList = adBillRepository.findByVideoAdAndCreatedAt(videoAd, today);
                adAmount += adBillList.stream().mapToDouble(AdBill::getTotalAmount).sum();
            }
            totalAmount = videoAmount + adAmount;
            overallTotalAmount += totalAmount;
            dailyBillDtoList.add(new DailyBillDto(video.getVideoId(), totalAmount, videoAmount, adAmount));
        }

        BillInfoDto billInfoDto = new BillInfoDto(user.getUserId(), overallTotalAmount, today, today);
        List<Object> finalResult = new ArrayList<>();
        finalResult.add(billInfoDto);
        finalResult.add(dailyBillDtoList);
        return finalResult;
    }

    public List<Object> searchWeeklyTotalAmount() throws Exception {
        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Video> myVideoList = videoRepository.findByUserUserId(user.getUserId());
        List<DailyBillDto> dailyBillDtoList = new ArrayList<>();
        double overallTotalAmount = 0.0;

        for (Video video : myVideoList) {
            double videoAmount = 0.0;
            double adAmount = 0.0;
            double totalAmount = 0.0;
            // 오늘의 동영상 정상비 가져오기
            List<VideoBill> videoBillList = videoBillRepository.findVideoBillsByDateRange(video, startOfWeek, endOfWeek);
            videoAmount = videoBillList.stream().mapToDouble(VideoBill::getTotalAmount).sum();
            // 비디오에 해당하는 모든 비디오 광고를 가져오고, 각 광고에 대한 오늘의 광고 정산비 가져오기
            List<VideoAd> videoAdList = videoAdRepository.findByVideo(video);
            for (VideoAd videoAd : videoAdList) {
                List<AdBill> adBillList = adBillRepository.findAdBillsByDateRange(videoAd, startOfWeek, endOfWeek);
                adAmount += adBillList.stream().mapToDouble(AdBill::getTotalAmount).sum();
            }
            totalAmount = videoAmount + adAmount;
            overallTotalAmount += totalAmount;
            dailyBillDtoList.add(new DailyBillDto(video.getVideoId(), totalAmount, videoAmount, adAmount));
        }

        BillInfoDto billInfoDto = new BillInfoDto(user.getUserId(), overallTotalAmount, startOfWeek, endOfWeek);
        List<Object> finalResult = new ArrayList<>();
        finalResult.add(billInfoDto);
        finalResult.add(dailyBillDtoList);
        return finalResult;
    }

    public List<Object> searchMonthlyTotalAmount() throws Exception {
        User user = getAuthenticatedUser();
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        List<Video> myVideoList = videoRepository.findByUserUserId(user.getUserId());
        List<DailyBillDto> dailyBillDtoList = new ArrayList<>();
        double overallTotalAmount = 0.0;

        for (Video video : myVideoList) {
            double videoAmount = 0.0;
            double adAmount = 0.0;
            double totalAmount = 0.0;
            // 오늘의 동영상 정상비 가져오기
            List<VideoBill> videoBillList = videoBillRepository.findVideoBillsByDateRange(video, startOfMonth, endOfMonth);
            videoAmount = videoBillList.stream().mapToDouble(VideoBill::getTotalAmount).sum();
            // 비디오에 해당하는 모든 비디오 광고를 가져오고, 각 광고에 대한 오늘의 광고 정산비 가져오기
            List<VideoAd> videoAdList = videoAdRepository.findByVideo(video);
            for (VideoAd videoAd : videoAdList) {
                List<AdBill> adBillList = adBillRepository.findAdBillsByDateRange(videoAd, startOfMonth, endOfMonth);
                adAmount += adBillList.stream().mapToDouble(AdBill::getTotalAmount).sum();
            }
            totalAmount = videoAmount + adAmount;
            overallTotalAmount += totalAmount;
            dailyBillDtoList.add(new DailyBillDto(video.getVideoId(), totalAmount, videoAmount, adAmount));
        }

        BillInfoDto billInfoDto = new BillInfoDto(user.getUserId(), overallTotalAmount, startOfMonth, endOfMonth);
        List<Object> finalResult = new ArrayList<>();
        finalResult.add(billInfoDto);
        finalResult.add(dailyBillDtoList);
        return finalResult;
    }

}
