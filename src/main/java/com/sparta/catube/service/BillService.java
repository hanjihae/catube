//package com.sparta.catube.service;
//
//import com.sparta.catube.dto.BillDto;
//import com.sparta.catube.entity.Bill;
//import com.sparta.catube.entity.User;
//import com.sparta.catube.entity.Video;
//import com.sparta.catube.entity.VideoAd;
//import com.sparta.catube.repository.BillRepository;
//import com.sparta.catube.repository.UserRepository;
//import com.sparta.catube.repository.VideoAdRepository;
//import com.sparta.catube.repository.VideoRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class BillService {
//
//    private final BillRepository billRepository;
//    private final UserRepository userRepository;
//    private final VideoRepository videoRepository;
//    private final VideoAdRepository videoAdRepository;
//
//    private User getAuthenticatedUser() throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Long authenticatedUserId = Long.parseLong(userDetails.getUsername());
//        return userRepository.findByUserId(authenticatedUserId)
//                .orElseThrow(() -> new Exception("ID를 찾을 수 없습니다."));
//    }
//
//    public void billForVideo() throws Exception {
//        User user = getAuthenticatedUser();
//        List<Video> videos = videoRepository.findByUserUserId(user.getUserId());
//        long videoAmount = 0;
//        long adsAmount = 0;
//        for (Video video : videos) {    // 동영상 총 조회수로 동영상 정산비 계산
//            int views = video.getVideoTotalViews();
//            if ( views >= 100000 && views < 500000) {
//                videoAmount = (long)(views * 1.1);
//            } else if ( views >= 500000 && views < 1000000) {
//                videoAmount = (long)(views * 1.3);
//            } else if (views <= 1000000) {
//                videoAmount = (long)(views * 1.5);
//            } else {
//                videoAmount = views;
//            }
//            List<VideoAd> vas = videoAdRepository.findByVideo(video);
//            for (VideoAd va : vas) {    // 동영상별 광고들의 조회수*단가를 곱해서 광고 총 정산비 계산
//                int adViews = va.getAdWatchedCount();
//                if ( adViews >= 100000 && adViews < 500000) {
//                    adsAmount += adViews * 12;
//                } else if ( adViews >= 500000 && adViews < 1000000) {
//                    adsAmount += adViews * 15;
//                } else if (adViews <= 1000000) {
//                    adsAmount += adViews * 20;
//                } else {
//                    adsAmount += adViews * 10;
//                }
//            }
//            long totalAmount = videoAmount + adsAmount;
//            // 동영상별 동영상 정산비, 광고 정산비, 총 정산비, 사용자 저장
//            Bill bill = Bill.of(videoAmount, adsAmount, totalAmount, video.getVideoId(), user);
//            billRepository.save(bill);
//        }
//    }
//
//    // 1일치 모든 동영상에 대한 정산금액 리스트
//    public BillDto billForAllVideosOfToday() throws Exception {
//        User user = getAuthenticatedUser();
//        List<Bill> bills = billRepository.findBillsCreatedTodayForUser(user.getUserId());
//        long totalAmount = 0;
//        long videoAmount = 0;
//        long adsAmount = 0;
//        for (Bill bill : bills) {
//            videoAmount += bill.getBillVideoAmount();
//            adsAmount += bill.getBillAdAmount();
//            totalAmount += bill.getBillTotalAmount();
//        }
//        return new BillDto(videoAmount, adsAmount, totalAmount, null, user.getUserId());
//    }
//
//    // 1일치 총 + 동영상별 정산금액 리스트
//    public List<List<BillDto>> billForToday() throws Exception {
//        User user = getAuthenticatedUser();
//        List<Bill> bills = billRepository.findBillsCreatedTodayForUser(user.getUserId());
//        List<List<BillDto>> billDtos = new ArrayList<>();
//        List<BillDto> totalBillDtos = new ArrayList<>();
//        List<BillDto> individualBillDtos = new ArrayList<>();
//        long totalAmount = 0L;
//        long videoAmount = 0L;
//        long adsAmount = 0L;
//
//        Map<Long, Long> videoAmounts = new HashMap<>();
//        Map<Long, Long> adAmounts = new HashMap<>();
//        Map<Long, Long> totalAmounts = new HashMap<>();
//
//        for (Bill bill : bills) {
//            videoAmount += bill.getBillVideoAmount();
//            adsAmount += bill.getBillAdAmount();
//            totalAmount += bill.getBillTotalAmount();
//
//            videoAmounts.put(bill.getVideoId(), videoAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillVideoAmount());
//            adAmounts.put(bill.getVideoId(), adAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillAdAmount());
//            totalAmounts.put(bill.getVideoId(), totalAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillTotalAmount());
//        }
//
//        totalBillDtos.add(new BillDto(videoAmount, adsAmount, totalAmount, null, user.getUserId()));
//
//
//        // 각 동영상 ID에 대해 집계된 금액을 저장
//        for (Map.Entry<Long, Long> entry : totalAmounts.entrySet()) {
//            Long videoId = entry.getKey();
//            long vAmount = videoAmounts.get(videoId);
//            long aAmount = adAmounts.get(videoId);
//            long tAmount = entry.getValue();
//
//            Bill newBill = Bill.of(vAmount, aAmount, tAmount, videoId, user);
//            individualBillDtos.add(new BillDto(vAmount, aAmount, tAmount, videoId, user.getUserId()));
//        }
//        billDtos.add(totalBillDtos);
//        billDtos.add(individualBillDtos);
//
//        return billDtos;
//    }
//
//    // 1주일치 총 + 동영상별 정산금액
//    public List<List<BillDto>> billForAllVideosOfWeek() throws Exception {
//        User user = getAuthenticatedUser();
//        List<Bill> bills = billRepository.findBillsCreatedThisWeekForUser(user.getUserId());
//        List<List<BillDto>> billDtos = new ArrayList<>();
//        List<BillDto> totalBillDtos = new ArrayList<>();
//        List<BillDto> individualBillDtos = new ArrayList<>();
//        long totalAmount = 0L;
//        long videoAmount = 0L;
//        long adsAmount = 0L;
//
//        Map<Long, Long> videoAmounts = new HashMap<>();
//        Map<Long, Long> adAmounts = new HashMap<>();
//        Map<Long, Long> totalAmounts = new HashMap<>();
//
//        for (Bill bill : bills) {
//            videoAmount += bill.getBillVideoAmount();
//            adsAmount += bill.getBillAdAmount();
//            totalAmount += bill.getBillTotalAmount();
//
//            videoAmounts.put(bill.getVideoId(), videoAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillVideoAmount());
//            adAmounts.put(bill.getVideoId(), adAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillAdAmount());
//            totalAmounts.put(bill.getVideoId(), totalAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillTotalAmount());
//        }
//
//        totalBillDtos.add(new BillDto(videoAmount, adsAmount, totalAmount, null, user.getUserId()));
//
//
//        // 각 동영상 ID에 대해 집계된 금액을 저장
//        for (Map.Entry<Long, Long> entry : totalAmounts.entrySet()) {
//            Long videoId = entry.getKey();
//            long vAmount = videoAmounts.get(videoId);
//            long aAmount = adAmounts.get(videoId);
//            long tAmount = entry.getValue();
//
//            Bill newBill = Bill.of(vAmount, aAmount, tAmount, videoId, user);
//            individualBillDtos.add(new BillDto(vAmount, aAmount, tAmount, videoId, user.getUserId()));
//        }
//        billDtos.add(totalBillDtos);
//        billDtos.add(individualBillDtos);
//
//        return billDtos;
//    }
//
//    // 1달치 총 + 동영상별 정산금액
//    public List<List<BillDto>> billForAllVideosOfMonth() throws Exception {
//        User user = getAuthenticatedUser();
//        List<Bill> bills = billRepository.findBillsCreatedThisMonthForUser(user.getUserId());
//        List<List<BillDto>> billDtos = new ArrayList<>();
//        List<BillDto> totalBillDtos = new ArrayList<>();
//        List<BillDto> individualBillDtos = new ArrayList<>();
//
//        long totalAmount = 0L;
//        long videoAmount = 0L;
//        long adsAmount = 0L;
//
//        Map<Long, Long> videoAmounts = new HashMap<>();
//        Map<Long, Long> adAmounts = new HashMap<>();
//        Map<Long, Long> totalAmounts = new HashMap<>();
//
//        for (Bill bill : bills) {
//            videoAmount += bill.getBillVideoAmount();
//            adsAmount += bill.getBillAdAmount();
//            totalAmount += bill.getBillTotalAmount();
//
//            videoAmounts.put(bill.getVideoId(), videoAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillVideoAmount());
//            adAmounts.put(bill.getVideoId(), adAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillAdAmount());
//            totalAmounts.put(bill.getVideoId(), totalAmounts.getOrDefault(bill.getVideoId(), 0L) + bill.getBillTotalAmount());
//        }
//        totalBillDtos.add(new BillDto(videoAmount, adsAmount, totalAmount, null, user.getUserId()));
//
//        for (Map.Entry<Long, Long> entry : totalAmounts.entrySet()) {
//            Long videoId = entry.getKey();
//            long vAmount = videoAmounts.get(videoId);
//            long aAmount = adAmounts.get(videoId);
//            long tAmount = entry.getValue();
//
//            Bill newBill = Bill.of(vAmount, aAmount, tAmount, videoId, user);
//            individualBillDtos.add(new BillDto(vAmount, aAmount, tAmount, videoId, user.getUserId()));
//        }
//        billDtos.add(totalBillDtos);
//        billDtos.add(individualBillDtos);
//
//        return billDtos;
//    }
//
//}
//
//
