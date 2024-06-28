package com.sparta.catube.controller;

import com.sparta.catube.dto.AdRequestDto;
import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.dto.WatchedVideoRequestDto;
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
    public ResponseEntity<VideoDto> createVideo(@RequestBody VideoRequestDto videoRequestDto) {
        try {
            VideoDto videoDto = videoService.createVideo(videoRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(videoDto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<List<VideoDto>> readMyVideoList() {
        List<VideoDto> videos = null;
        try {
            videos = videoService.readVideoList();
            return ResponseEntity.status(HttpStatus.OK).body(videos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable Long videoId, @RequestBody VideoRequestDto videoRequestDto) {
        try {
            VideoDto videoDto = videoService.updateVideo(videoId, videoRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(videoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{videoId}")
    public String deleteVideo(@PathVariable Long videoId) {
        try {
            videoService.deleteVideo(videoId);
            return "삭제 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "삭제 실패" + e.getMessage();
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
        try {
            VideoDto videoDto = videoService.stopVideo(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(videoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register-ad/{videoId}")
    public String registerAd(@PathVariable Long videoId, @RequestBody List<AdRequestDto> adRequestDto) {
        try {
            videoService.insertAdsIntoVideo(videoId, adRequestDto);
            return "광고 등록 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "광고 등록 실패: " + e.getMessage();
        }
    }
}
