package com.sparta.catube.repository;

import com.sparta.catube.entity.AdStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdStatRepository extends JpaRepository<AdStat, Long> {
    @Query("SELECT a.videoAd, a.dailyViewCount " +
            "FROM AdStat a " +
            "WHERE a.createdAt = :today")
    List<Object[]> findDailyViewCount(@Param("today") LocalDate today);
}
