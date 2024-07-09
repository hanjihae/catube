package com.sparta.catube.repository;

import com.sparta.catube.entity.Video;
import com.sparta.catube.entity.VideoBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoBillRepository extends JpaRepository<VideoBill, Long> {

    List<VideoBill> findByVideoAndCreatedAt(Video video, LocalDate today);

    @Query("SELECT v FROM VideoBill v " +
            "WHERE v.createdAt >= :start AND v.createdAt <= :end " +
            "AND v.video = :video")
    List<VideoBill> findVideoBillsByDateRange(Video video, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
