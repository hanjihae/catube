package com.sparta.catube.repository;

import com.sparta.catube.entity.Ad;
import com.sparta.catube.entity.AdsList;
import com.sparta.catube.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    @Query("SELECT a FROM Ad a WHERE a.videoAd = :videoAd")
    Ad findByVideoAd(@Param("videoAd") VideoAd videoAd);
}
