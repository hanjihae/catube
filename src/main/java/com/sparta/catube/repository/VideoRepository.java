package com.sparta.catube.repository;

import com.sparta.catube.entity.User;
import com.sparta.catube.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
