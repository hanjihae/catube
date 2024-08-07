package com.sparta.catube.repository;

import com.sparta.catube.entity.Video;
import com.sparta.catube.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoAdRepository extends JpaRepository<VideoAd, Long> {
    List<VideoAd> findByVideo(Video video);

    @Query("SELECT va, COUNT(av) " +
            "FROM VideoAd va " +
            "JOIN va.adViews av " +
            "WHERE FUNCTION('DATE_FORMAT', av.createdAt, '%Y%m%d') = FUNCTION('DATE_FORMAT', :today, '%Y%m%d') " +
            "GROUP BY va.videoAdId")
    List<Object[]> countGroupedByVideoIdAndVideoAdId(@Param("today") LocalDate today);
}
