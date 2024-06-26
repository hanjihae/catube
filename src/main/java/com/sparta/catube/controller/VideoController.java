package com.sparta.catube.controller;

import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.entity.Video;
import com.sparta.catube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
