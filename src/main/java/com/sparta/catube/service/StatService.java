package com.sparta.catube.service;

import com.sparta.catube.repository.StatisticsRepository;
import com.sparta.catube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatisticsRepository statisticsRepository;
    private final VideoRepository videoRepository;



}
