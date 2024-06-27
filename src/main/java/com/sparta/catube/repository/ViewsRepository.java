package com.sparta.catube.repository;

import com.sparta.catube.entity.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Long> {
    Optional<Views> findByUser_UserIdAndVideo_VideoId(Long userId, Long videoId);
    int countByVideo_VideoId(Long videoId);
}
