package com.sparta.catube.service;

import com.sparta.catube.dto.BillInfoDto;
import com.sparta.catube.dto.DailyBillDto;
import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import jakarta.transaction.Transactional;
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
    private final VideoRepository videoRepository;
    private final VideoAdRepository videoAdRepository;
    private final VideoBillRepository videoBillRepository;
    private final AdBillRepository adBillRepository;

    public LocalDate today = LocalDate.now().minusDays(1);

    private User getAuthenticatedUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long authenticatedUserId = Long.parseLong(userDetails.getUsername());
        return userRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new Exception("ID를 찾을 수 없습니다."));
    }

    public List<Object> searchDailyTotalAmount() throws Exception {
        User user = getAuthenticatedUser();

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
