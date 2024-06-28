package com.sparta.catube.service;

import com.sparta.catube.dto.AdRequestDto;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.dto.WatchedVideoRequestDto;
import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
    }

    private Video getVideo(Long videoId) throws Exception {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new Exception("해당 비디오가 존재하지 않습니다."));
    }

    public Video createVideo(VideoRequestDto videoRequestDto) throws Exception {
        User user = getAuthenticatedUser();
        if (!user.getUserId().equals(videoRequestDto.getUserId())) {
            throw new Exception("귀하의 ID가 인증된 사용자의 ID와 일치하지 않습니다.");
        }
        if (user.getUserType().equals("USER")) {
            user.setUserType("SELLER");
        }
        if (videoRequestDto.getVideoLength() > 86400) { // 동영상의 길이가 24시간 이상이면
            throw new Exception("용량 초과입니다.");
        }
        // 새로운 동영상 만들기
        Video video = Video.of(user, videoRequestDto);
        return videoRepository.save(video);
    }

    public VideoDto watchVideo(Long videoId) throws Exception {
        Video video = getVideo(videoId);
        User user = getAuthenticatedUser();
        Long authenticatedUserId = user.getUserId();
        // 현재 사용자의 시청기록 조회
        Views views = viewsRepository.findByUser_UserIdAndVideo_VideoId(authenticatedUserId, videoId)
                .orElseGet(() -> {  // 시청기록이 없으면
                    Views view = Views.createNewView(user, video, authenticatedUserId);
                    return viewsRepository.save(view);    // 새로운 시청기록 저장
                });
        // 본인이 본인 동영상을 보면 조회수 카운트 X
        // 동영상 업로더의 ID와 인증된 사용자 ID를 비교해서 일치하지 않을 때
        if (!video.getUser().getUserId().equals(authenticatedUserId)) {
            // 시청기록이 24시간 내에 없다면 조회수 +1
            if (views.getUpdatedAt().isBefore(LocalDateTime.now().minusDays(1))) {
                views.setViewsCount(views.getViewsCount() + 1);
                viewsRepository.save(views);
            }
            int totalViews = viewsRepository.countByVideo_VideoId(videoId);
            video.setVideoTotalViews(totalViews);
            videoRepository.save(video);
        }
        return video.toDto();
    }

    @Transactional
    public VideoDto stopVideo(WatchedVideoRequestDto requestDto) throws Exception {
        Video video = getVideo(requestDto.getVideoId());
        User user = getAuthenticatedUser();
        // 현재 사용자의 시청 기록 조회
        Views views = viewsRepository.findByUser_UserIdAndVideo_VideoId(user.getUserId(), requestDto.getVideoId())
                .orElseThrow(() -> new Exception("사용자의 시청 기록을 찾을 수 없습니다."));
        long playTime = 0; // 동영상 시청 시간
        // 마지막 재생시점이 동영상 길이보다 길다면
        if (requestDto.getLastWatchedTime() >= video.getVideoLength()) {
            // 동영상 시청 시간 = 동영상 길이 - 저장되었던 재생시점
            playTime = video.getVideoLength() - views.getViewsLastWatchedTime();
            // 처음부터 재생되도록 마지막 재생시점 0 저장
            views.setViewsLastWatchedTime(0);
        } else {
            // 동영상 시청 시간 = 현 재생시점 - 저장되었던 재생시점
            playTime = requestDto.getLastWatchedTime() - views.getViewsLastWatchedTime();
            // 마지막 재생시간 갱신
            views.setViewsLastWatchedTime(requestDto.getLastWatchedTime());
        }
        long totalPlayTimeOfYou = views.getViewsPlaytime() + playTime;
        long totalPlayTimeOfVideo = video.getVideoTotalPlaytime() + playTime;
        // 마지막 재생시점 저장
        views.setViewsPlaytime(totalPlayTimeOfYou);// 시청기록 갱신
        video.setVideoTotalPlaytime(totalPlayTimeOfVideo); // 해당 동영상의 총 재생시간 갱신
        videoRepository.save(video);
        viewsRepository.save(views);

        return video.toDto();
    }

    @Transactional
    public boolean insertAdsIntoVideo(Long videoId, List<AdRequestDto> adRequestDto) throws Exception {
        try {
            Video video = getVideo(videoId);
            User user = getAuthenticatedUser();
            if (!video.getUser().getUserId().equals(user.getUserId())) {
                throw new Exception("귀하의 ID가 인증된 사용자의 ID와 일치하지 않습니다.");
            }
            // 광고 개수 = 동영상 길이 / 5분
            int adCount = (int) video.getVideoLength() / 300;
            for (int i=1; i <= adCount; i++) {
                long position = i * 300;
                VideoAd videoAd = new VideoAd();
                videoAd.setVideo(video);
                videoAd.setVaPosition(position);
                videoAdRepository.save(videoAd);
            }
            // 해당 동영상에 삽입된 VideoAd 리스트
            List<VideoAd> vas = videoAdRepository.findByVideo(video);
            int adIndex = 0;
            for (VideoAd va : vas) {
                if (adCount > adRequestDto.size()) {    // 넣을 수 있는 광고 개수보다 광고리스트에 있는 광고 개수보다 적다면
                    throw new Exception(adCount - adRequestDto.size() + "개만큼 등록할 광고 개수가 부족합니다.");
                }
                AdRequestDto adDto = adRequestDto.get(adIndex++);
                Ad ad = new Ad();
                ad.setAdUrl(adDto.getAdUrl());
                ad.setAdLength(adDto.getAdLength());
                adRepository.save(ad);
                va.setAd(ad);
                videoAdRepository.save(va);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
