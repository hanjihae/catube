package com.sparta.catube.repository;

import com.sparta.catube.entity.Video;
import com.sparta.catube.entity.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Long> {
    Optional<Views> findByUser_UserIdAndVideo_VideoId(Long userId, Long videoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Views v WHERE v.video = :video")
    void deleteByVideo(Video video);
}
