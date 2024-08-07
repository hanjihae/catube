package com.sparta.catube.controller;

import com.sparta.catube.dto.VideoDto;
import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<VideoDto> createVideo(@RequestBody VideoRequestDto videoRequestDto) throws Exception {
        VideoDto videoDto = videoService.createVideo(videoRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(videoDto);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<VideoDto>> readMyVideoList() throws Exception {
        List<VideoDto> videos = videoService.readVideoList();
        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(videos);
    }

    @PutMapping("/update/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable Long videoId, @RequestBody VideoRequestDto videoRequestDto) throws Exception{
        VideoDto videoDto = videoService.updateVideo(videoId, videoRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(videoDto);
    }

    @DeleteMapping("/delete/{videoId}")
    public String deleteVideo(@PathVariable Long videoId) throws Exception {
        videoService.deleteVideo(videoId);
        return "동영상을 삭제했습니다.";
    }

    @PostMapping("/watch/{videoId}")
    public ResponseEntity<VideoDto> watchVideo(@PathVariable Long videoId) throws Exception {
        VideoDto videoDto = videoService.watchVideo(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(videoDto);
    }

    @PutMapping("/stop/{videoId}")
    public ResponseEntity<VideoDto> stopVideo(@PathVariable Long videoId, @RequestBody long lastWatchedTime) throws Exception {
        VideoDto videoDto = videoService.stopVideo(videoId, lastWatchedTime);
        return ResponseEntity.status(HttpStatus.OK).body(videoDto);
    }
}
