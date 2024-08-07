package com.sparta.catube.repository;

import com.sparta.catube.entity.AdViews;
import com.sparta.catube.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdViewsRepository extends JpaRepository<AdViews, Long> {

    List<AdViews> findByVideoAd(VideoAd va);

}
