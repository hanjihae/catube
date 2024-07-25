package com.sparta.catube.service;

import com.sparta.catube.common.ErrorMessage;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.entity.*;
import com.sparta.catube.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final RedissonClient redissonClient;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ViewsRepository viewsRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdViewsRepository adViewsRepository;
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
            List<Ad> ads = adRepository.findAll();
            List<VideoAd> vaListToSave = new ArrayList<>();
            // 광고 개수 = 동영상 길이 / 5분
            int cnt = (int) video.getVideoLength() / (5 * 60);
            int adCount = (int) video.getVideoLength() % (5 * 60) == 0 ?  cnt - 1 : cnt;
            if (adCount <= 0) {
                throw new Exception("동영상 길이가 짧아 광고를 삽입할 수 없습니다.");
            }
            Random random = new Random();
            for (int i=1; i <= adCount; i++) {
                int index = random.nextInt(adCount);
                Ad ad = ads.get(index);
                VideoAd videoAd = VideoAd.of(video, ad);
                vaListToSave.add(videoAd);
            }
            videoAdRepository.saveAll(vaListToSave);
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
            for (VideoAd va : vas) {
                List<AdViews> adViews = adViewsRepository.findByVideoAd(va);
                adViewsRepository.deleteAll(adViews);
            }
            videoAdRepository.deleteAll(vas);
            List<Views> viewsList = viewsRepository.findByVideo(video);
            viewsRepository.deleteAll(viewsList);
            videoRepository.delete(video); // 동영상 삭제
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_DELETE_VIDEO.getMessage());
        }
    }

    @Transactional
    public VideoDto watchVideo(Long videoId) throws Exception {
//        String lockKey = "video:lock:" + videoId;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            boolean isLocked = lock.tryLock(500, 1500, TimeUnit.MILLISECONDS); // 락 획득 시간 설정
//            if (!isLocked) {
//                throw new Exception("Unable to acquire lock");
//            }

            Video video = getVideo(videoId);
            User user = getAuthenticatedUser();
            try {
                // 본인이 본인 동영상을 보면 조회수 카운트 X - 누적조회수는 배치 돌리고 나서 시청기록 세서 한꺼번에 플러스
                // 동영상 업로더의 ID와 인증된 사용자 ID를 비교해서 일치하지 않을 때
//            if (!video.getUser().getUserId().equals(user.getUserId())) {
//                Optional<Views> recentViewsOptional = viewsRepository.findLatestViewByUserAndVideo(user, video);
//                if (recentViewsOptional.isPresent()) {  // 이전 시청기록이 있다면
//                    Views recentViews = recentViewsOptional.get();
                // 해당 동영상에 대한 개인의 시청기록 생성일자가 현재 시간보다 30초 이후일 때 +1 카운트
//                  if (recentViews.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
//                        // 시청기록 생성
//                        Views views = Views.of(user, video);
//                        viewsRepository.save(views);
//                    }
//                } else {    // 이전 시청기록이 없다면
                // 시청기록 생성
                Views views = Views.of(user, video);
                viewsRepository.save(views);
//                }
//            }
                return new VideoDto(video);
            } catch (Exception e) {
                throw new Exception(ErrorMessage.FAILED_WATCH_VIDEO.getMessage());
            }
//        } catch (Exception e) {
//            throw new Exception("Failed to watch video with id: " + videoId, e);
//        } finally {
//            lock.unlock();
//        }
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
        List<VideoAd> vasToSave = new ArrayList<>();
        List<AdViews> adViewsToSave = new ArrayList<>();
        for (int i = 1; i <= vas.size(); i++) {
            VideoAd va = vas.get(i-1);
            int position = i * (60 * 5);
            // 저장된 마지막 재생시점보다 크고, 전송된 마지막 재생시점보다 작은 경우 카운트
            if (position > previousViews.getViewsLastWatchedTime() && position <= lastWatchedTime) {
                AdViews adViews = AdViews.of(va, va.getAd());   // 광고 시청기록 생성 - 배치할 때 va.getAdWatchedCount에 한꺼번에 조회수 저장
                adViewsToSave.add(adViews);
            }
        }
        // 동영상 시청 시간
        long playTime = 0;
        long previousLastWatchedTime = previousViews.getViewsLastWatchedTime();
        // 마지막 재생시점이 동영상 길이보다 길다면
        if (lastWatchedTime >= video.getVideoLength()) {
            // 동영상 시청 시간 = 동영상 길이 - 저장되었던 재생시점
            playTime = video.getVideoLength() - previousLastWatchedTime;
            // 처음부터 재생되도록 마지막 재생시점 0 저장
            views.saveViewsLastWatchedTime(0);
        } else {
            // 동영상 시청 시간 = 현 재생시점 - 저장되었던 재생시점
            playTime = lastWatchedTime - previousLastWatchedTime;
            // 마지막 재생시간 갱신
            views.saveViewsLastWatchedTime(lastWatchedTime);
        }
        // 마지막 재생시점 저장
        views.saveViewsPlaytime(playTime);
        viewsRepository.save(views);
        try {
            // 본인이 만든 동영상이 아닐 때
//            if (!video.getUser().getUserId().equals(user.getUserId())) {
                // 30초 전에 본 시청기록이 있다면
//                if (previousViews.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(30))) {
                    adViewsRepository.saveAll(adViewsToSave);   // 광고 시청기록 생성
                    videoAdRepository.saveAll(vasToSave);   // 각 광고 누적조회수 갱신
//                }
//            }
            return new VideoDto(video);
        } catch (Exception e) {
            throw new Exception(ErrorMessage.FAILED_STOP_VIDEO.getMessage());
        }
    }
}
