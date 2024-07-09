package com.sparta.catube.repository;

import com.sparta.catube.entity.VideoStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoStatRepository extends JpaRepository<VideoStat, Long> {

    @Query("SELECT v.video, SUM(v.dailyViewCount) cnt FROM VideoStat v " +
            "WHERE v.createdAt = :today " +
            "GROUP BY v.video ORDER BY cnt DESC LIMIT 5")
    List<Object[]> findTodayVideoViewCount(@Param("today")LocalDate today);

    @Query("SELECT v.video, SUM(v.dailyPlayTime) pt FROM VideoStat v " +
            "WHERE v.createdAt = :today " +
            "GROUP BY v.video ORDER BY pt DESC LIMIT 5")
    List<Object[]> findTodayVideoPlayTime(@Param("today")LocalDate today);

    @Query("SELECT v.video, SUM(v.dailyViewCount) cnt FROM VideoStat v " +
            "WHERE v.createdAt >= :start AND v.createdAt < :end " +
            "GROUP BY v.video ORDER BY cnt DESC LIMIT 5")
    List<Object[]> findVideoViewCount(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT v.video, SUM(v.dailyPlayTime) pt FROM VideoStat v " +
            "WHERE v.createdAt >= :start AND v.createdAt < :end " +
            "GROUP BY v.video ORDER BY pt DESC LIMIT 5")
    List<Object[]> findVideoPlayTime(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
