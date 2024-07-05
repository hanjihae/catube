package com.sparta.catube.service;

import com.sparta.catube.common.ErrorMessage;
import com.sparta.catube.dto.*;
import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ViewsRepository viewsRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdRepository adRepository;
    private final AdsListRepository adsListRepository;

    private User getAuthenticatedUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long authenticatedUserId = Long.parseLong(userDetails.getUsername());
        return userRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new Exception("ID를 찾을 수 없습니다."));
    }

    private Video getVideo(Long videoId) throws Exception {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new Exception("존재하지 않는 동영상입니다."));
    }

    @Transactional
    public VideoDto createVideo(VideoRequestDto videoRequestDto) throws Exception {
        User user = getAuthenticatedUser();
        if (user.getUserType().equals("USER")) {
            user.saveUserType("SELLER");
        }
        if (videoRequestDto.getVideoLength() > 86400) { // 동영상의 길이가 24시간 이상이면
            throw new Exception("용량 초과입니다.");
        }
        // 새로운 동영상 만들기
        Video video = Video.of(user, videoRequestDto);
        try {
            video = videoRepository.save(video);
            insertAdsIntoVideo(video.getVideoId());
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_CREATE_VIDEO.getMessage());
        }
    }

    @Transactional
    public void insertAdsIntoVideo(Long videoId) throws Exception {
        Video video = getVideo(videoId);
        try {
            List<AdsList> adsList = adsListRepository.findAll();    // 광고리스트 모두 가져오기
            List<VideoAd> vasToSave = new ArrayList<>();    // video - ad 중간테이블
            List<Ad> adsToSave = new ArrayList<>();  // 광고 - 광고 조회수/ 광고 위치 포함
            // 광고 개수 = 동영상 길이 / 5분
            int cnt = (int) video.getVideoLength() / (5 * 60);
            int adCount = (int) video.getVideoLength() % (5 * 60) == 0 ?  cnt - 1 : cnt;
            if (adCount <= 0) {
                throw new Exception("동영상 길이가 짧아 광고를 삽입할 수 없습니다.");
            }
            Random random = new Random();
            for (int i=1; i <= adCount; i++) {
                long position = i * (5 * 60);   // 광고 삽입 위치
                int randomAdIndex = random.nextInt(adsList.size()); // 광고정보리스트에서 꺼내올 광고정보의 인덱스
                AdsList ads = adsList.get(randomAdIndex);   // 랜덤으로 꺼내온 광고정보
                Ad ad = Ad.of(ads, position);   // 광고에 광고정보와 광고삽입위치 넣어 광고 생성
                VideoAd videoAd = VideoAd.of(video, ad);    // 만들어진 ad, video-ad에 넣기
                ad.saveVideoAd(videoAd);    // va 정보 ad에도 넣기
                adsToSave.add(ad);
                vasToSave.add(videoAd);
            }
            adRepository.saveAll(adsToSave);
            videoAdRepository.saveAll(vasToSave);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_REGISTER_AD.getMessage());
        }
    }

    @Transactional
    public List<VideoDto> readVideoList() throws Exception {
        User user = getAuthenticatedUser();
        try {
            List<Video> videos = videoRepository.findByUserUserId(user.getUserId());
            return videos.stream()
                    .map(VideoDto::new) // Video 엔티티를 VideoDto로 변환
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_READ_VIDEO.getMessage());
        }
    }

    @Transactional
    public VideoDto updateVideo(Long videoId, VideoRequestDto videoRequestDto) throws Exception {
        User user = getAuthenticatedUser();
        Video video = getVideo(videoId);
        if (!video.getUser().getUserId().equals(user.getUserId())) {
            throw new Exception(video.getVideoTitle() + "을 수정할 수 있는 권한이 없습니다.");
        }
        // 변경된 동영상 정보 갱신
        video.update(videoRequestDto);
        try {
            videoRepository.save(video);
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_UPDATE_VIDEO.getMessage());
        }
    }

    @Transactional
    public void deleteVideo(Long videoId) throws Exception {
        User user = getAuthenticatedUser();
        Video video = getVideo(videoId);
        if (!video.getUser().getUserId().equals(user.getUserId())) {
            throw new Exception(video.getVideoTitle() + "을 삭제할 권한이 없습니다.");
        }
        try {
            // 동영상에 연결된 광고들 삭제
            List<VideoAd> vas = videoAdRepository.findByVideo(video);
            List<Ad> ads = new ArrayList<>();
            for (VideoAd va : vas) {
                Ad ad = adRepository.findByVideoAd(va);
                ads.add(ad);
            }
            videoAdRepository.deleteAll(vas);
            adRepository.deleteAll(ads);
            List<Views> viewsList = viewsRepository.findByVideo(video);
            viewsRepository.deleteAll(viewsList);
            videoRepository.delete(video); // 동영상 삭제
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_DELETE_VIDEO.getMessage());
        }
    }

    @Transactional
    public VideoDto watchVideo(Long videoId) throws Exception {
        Video video = getVideo(videoId);
        User user = getAuthenticatedUser();
        try {
            // 시청기록 생성
            Views views = Views.of(user, video);
            viewsRepository.save(views);
            // 본인이 본인 동영상을 보면 조회수 카운트 X
            // 동영상 업로더의 ID와 인증된 사용자 ID를 비교해서 일치하지 않을 때
            if (!video.getUser().getUserId().equals(user.getUserId())) {
                // 해당 동영상에 대한 개인의 시청기록 생성일자가 현재 시간보다 30초 이후일 때 +1 카운트
                Optional<Views> recentViewsOptional = viewsRepository.findLatestViewByUserAndVideo(user, video);
                if (recentViewsOptional.isPresent()) {  // 이전 시청기록이 있다면
                    Views recentViews = recentViewsOptional.get();
                    if (recentViews.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
                        video.saveVideoTotalViews(video.getVideoTotalViews() + 1);
                        videoRepository.save(video);
                    }
                } else {    // 이전 시청기록이 없다면
                    video.saveVideoTotalViews(video.getVideoTotalViews() + 1);
                    videoRepository.save(video);
                }
            }
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_WATCH_VIDEO.getMessage());
        }
    }

    @Transactional
    public VideoDto stopVideo(Long videoId, long lastWatchedTime) throws Exception {
        User user = getAuthenticatedUser();
        Video video = getVideo(videoId);
        // 이전 시청기록 조회
        List<Views> recentViews = viewsRepository.find2ViewsByUserAndVideo(user, video);
        Views views = recentViews.get(0);  // 비디오 생성했을 때 만들어진 시청기록
        Views previousViews;    // 이전 시청기록
        if (recentViews.size() == 1) {  // 만약 이전 시청기록이 없다면
            previousViews = views;
        } else {
            previousViews = recentViews.get(1);
        }
        // 심어놓은 광고 리스트 조회
        List<VideoAd> vas = videoAdRepository.findByVideo(video);
        List<Ad> adsToSave = new ArrayList<>();
        for (VideoAd va : vas) {
            // video에 해당되는 VideoAd에 저장된 adId로 ad 가져옴
            Ad ad = adRepository.findByVideoAd(va);
            // 저장된 마지막 재생시점보다 크고, 전송된 마지막 재생시점보다 작은 경우 카운트
            if (ad.getVaPosition() > previousViews.getViewsLastWatchedTime() && ad.getVaPosition() <= lastWatchedTime) {
                ad.saveAdWatchedCount(ad.getAdWatchedCount() + 1);
                adsToSave.add(ad);
            }
        }
        // 동영상 시청 시간
        long playTime = 0;
        // 마지막 재생시점이 동영상 길이보다 길다면
        if (lastWatchedTime >= video.getVideoLength()) {
            // 동영상 시청 시간 = 동영상 길이 - 저장되었던 재생시점
            playTime = video.getVideoLength() - previousViews.getViewsLastWatchedTime();
            // 처음부터 재생되도록 마지막 재생시점 0 저장
            views.saveViewsLastWatchedTime(0);
        } else {
            // 동영상 시청 시간 = 현 재생시점 - 저장되었던 재생시점
            playTime = lastWatchedTime - previousViews.getViewsLastWatchedTime();
            // 마지막 재생시간 갱신
            views.saveViewsLastWatchedTime(lastWatchedTime);
        }
        long totalPlayTimeOfVideo = video.getVideoTotalPlaytime() + playTime;
        // 마지막 재생시점 저장
        views.saveViewsPlaytime(playTime);
        viewsRepository.save(views);
        try {
            if (!video.getUser().getUserId().equals(user.getUserId())) {
                if (previousViews.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
                    adRepository.saveAll(adsToSave);
                    video.saveVideoTotalPlaytime(totalPlayTimeOfVideo); // 해당 동영상의 총 재생시간 갱신
                    videoRepository.save(video);
                }
            }
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_STOP_VIDEO.getMessage());
        }
    }
}
