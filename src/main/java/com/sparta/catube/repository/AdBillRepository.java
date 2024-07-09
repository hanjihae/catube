package com.sparta.catube.repository;

import com.sparta.catube.entity.AdBill;
import com.sparta.catube.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdBillRepository extends JpaRepository<AdBill, Long> {
    List<AdBill> findByVideoAdAndCreatedAt(VideoAd videoAd, LocalDate today);

    @Query("SELECT a FROM AdBill a " +
            "WHERE a.createdAt >= :start AND a.createdAt <= :end " +
            "AND a.videoAd = :videoAd")
    List<AdBill> findAdBillsByDateRange(VideoAd videoAd, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
