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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ViewsRepository viewsRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdRepository adRepository;

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
            videoRepository.save(video);
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_CREATE_VIDEO.getMessage());
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
            for (VideoAd va : vas) {
                adRepository.delete(va.getAd());
            }
            videoAdRepository.deleteAll(vas);
            viewsRepository.deleteByVideo(video);   // 동영상 시청기록들 삭제
            videoRepository.delete(video); // 동영상 삭제
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_DELETE_VIDEO.getMessage());
        }
    }

    @Transactional
    public VideoDto watchVideo(Long videoId) throws Exception {
        Video video = getVideo(videoId);
        User user = getAuthenticatedUser();
        Long authenticatedUserId = user.getUserId();
        try {
            // 현재 사용자의 시청기록 조회
            Views views = viewsRepository.findByUser_UserIdAndVideo_VideoId(authenticatedUserId, videoId)
                    .orElseGet(() -> {  // 시청기록이 없으면
                        Views view = Views.of(user, video, authenticatedUserId);
                        return viewsRepository.save(view);    // 새로운 시청기록 저장
                    });
            // 본인이 본인 동영상을 보면 조회수 카운트 X
            // 동영상 업로더의 ID와 인증된 사용자 ID를 비교해서 일치하지 않을 때
            if (!video.getUser().getUserId().equals(authenticatedUserId)) {
                // 해당 동영상에 대한 개인의 시청기록 업뎃 time이 현재 시간보다 30초 이후일 때 +1 카운트
                if (views.getUpdatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
                    views.saveViewsCount(views.getViewsCount() + 1);
                    viewsRepository.save(views);
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
        // 현재 사용자의 시청 기록 조회
        Views views = viewsRepository.findByUser_UserIdAndVideo_VideoId(user.getUserId(), videoId)
                .orElseThrow(() -> new Exception(video.getVideoTitle() + "에 대한 시청기록이 존재하지 않습니다."));
        // 심어놓은 광고 리스트 조회
        List<VideoAd> vas = videoAdRepository.findByVideo(video);
        List<Ad> adsToSave = new ArrayList<>();
        for (VideoAd va : vas) {
            Ad ad = va.getAd();
            // 저장된 마지막 재생시점보다 크고, 전송된 마지막 재생시점보다 작은 경우 카운트
            if (va.getVaPosition() > views.getViewsLastWatchedTime() && va.getVaPosition() <= lastWatchedTime) {
                ad.saveAdWatchedCount(ad.getAdWatchedCount() + 1);
                adsToSave.add(ad);
            }
        }
        // 동영상 시청 시간
        long playTime = 0;
        // 마지막 재생시점이 동영상 길이보다 길다면
        if (lastWatchedTime >= video.getVideoLength()) {
            // 동영상 시청 시간 = 동영상 길이 - 저장되었던 재생시점
            playTime = video.getVideoLength() - views.getViewsLastWatchedTime();
            // 처음부터 재생되도록 마지막 재생시점 0 저장
            views.saveViewsLastWatchedTime(0);
        } else {
            // 동영상 시청 시간 = 현 재생시점 - 저장되었던 재생시점
            playTime = lastWatchedTime - views.getViewsLastWatchedTime();
            // 마지막 재생시간 갱신
            views.saveViewsLastWatchedTime(lastWatchedTime);
        }
        long totalPlayTimeOfYou = views.getViewsPlaytime() + playTime;
        long totalPlayTimeOfVideo = video.getVideoTotalPlaytime() + playTime;
        // 마지막 재생시점 저장
        views.saveViewsPlaytime(totalPlayTimeOfYou);// 시청기록 갱신
        video.saveVideoTotalPlaytime(totalPlayTimeOfVideo); // 해당 동영상의 총 재생시간 갱신
        try {
            if (!video.getUser().getUserId().equals(user.getUserId())) {
                if (views.getUpdatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
                    adRepository.saveAll(adsToSave);
                    videoRepository.save(video);
                    viewsRepository.save(views);
                }
            }
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_STOP_VIDEO.getMessage());
        }
    }

    @Transactional
    public void insertAdsIntoVideo(Long videoId, List<AdRequestDto> adRequestDto) throws Exception {
        Video video = getVideo(videoId);
        User user = getAuthenticatedUser();
        if (!video.getUser().getUserId().equals(user.getUserId())) {
            throw new Exception(video.getVideoTitle() + "에 광고를 등록할 수 있는 권한이 없습니다.");
        }
        try {
            // 광고 개수 = 동영상 길이 / 5분
            int cnt = (int) video.getVideoLength() / 300;
            int adCount = (int) video.getVideoLength() % 300 == 0 ?  cnt - 1 : cnt;
            if (adCount > adRequestDto.size()) {    // 넣을 수 있는 광고 개수보다 광고리스트에 있는 광고 개수보다 적다면
                throw new Exception( video.getVideoTitle() + "에 등록할 광고 개수가 부족합니다. \n 필요한 광고 개수 : " + adCount);
            }
            for (int i=1; i <= adCount; i++) {
                long position = i * 300;
                VideoAd videoAd = VideoAd.createVideoAd(video, position);
                videoAdRepository.save(videoAd);
            }
            // 해당 동영상에 삽입된 VideoAd 리스트
            List<VideoAd> vas = videoAdRepository.findByVideo(video);
            List<VideoAd> vasToSave = new ArrayList<>();
            List<Ad> adsToSave = new ArrayList<>();
            for (int i = 0; i < vas.size(); i++) {
                if (i < adRequestDto.size()) {
                    AdRequestDto adDto = adRequestDto.get(i);
                    Ad ad = Ad.of(adDto);
                    adsToSave.add(ad);
                    VideoAd va = vas.get(i);
                    va.saveAd(ad);
                    vasToSave.add(va);
                } else {
                    break;
                }
            }
            adRepository.saveAll(adsToSave);
            videoAdRepository.saveAll(vasToSave);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_REGISTER_AD.getMessage());
        }
    }
}
