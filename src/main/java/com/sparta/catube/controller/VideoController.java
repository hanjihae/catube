package com.sparta.catube.controller;

import com.sparta.catube.dto.AdRequestDto;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.dto.WatchedVideoRequestDto;
import com.sparta.catube.entity.Video;
import com.sparta.catube.repository.VideoRepository;
import com.sparta.catube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<String> createVideo(@RequestBody VideoRequestDto videoRequestDto) {
        try {
            Video video = videoService.createVideo(videoRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(video.getVideoUrl());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/watch/{videoId}")
    public ResponseEntity<VideoDto> watchVideo(@PathVariable Long videoId) {
        try {
            VideoDto videoDto = videoService.watchVideo(videoId);
            return ResponseEntity.status(HttpStatus.OK).body(videoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/stop")
    public ResponseEntity<VideoDto> stopVideo(@RequestBody WatchedVideoRequestDto requestDto) throws Exception {
        VideoDto videoDto = videoService.stopVideo(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(videoDto);
    }

    @PostMapping("/register-ad")
    public String registerAd(@RequestBody Long videoId, @RequestBody List<AdRequestDto> adRequestDto) {
        try {
            boolean result = videoService.insertAdsIntoVideo(videoId, adRequestDto);
            return result ? "광고 등록 완료" : "광고 등록 실패";
        } catch (Exception e) {
            e.printStackTrace();
            return "광고 등록 실패: " + e.getMessage();
        }
    }

}
