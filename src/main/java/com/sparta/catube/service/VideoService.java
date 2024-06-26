package com.sparta.catube.service;

import com.sparta.catube.dto.VideoRequestDto;
import com.sparta.catube.entity.User;
import com.sparta.catube.entity.Video;
import com.sparta.catube.repository.UserRepository;
import com.sparta.catube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public Video createVideo(VideoRequestDto videoRequestDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authenticatedUserId = Long.parseLong(authentication.getName());

        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new Exception("인증된 사용자를 찾을 수 없습니다."));
        if (!authenticatedUser.getUserId().equals(videoRequestDto.getUserId())) {
            throw new Exception("귀하의 ID가 인증된 사용자의 ID와 일치하지 않습니다.");
        }

        Video video = Video.of(authenticatedUser, videoRequestDto);

        return videoRepository.save(video);
    }

}
