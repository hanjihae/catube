package com.sparta.catube.repository;

import com.sparta.catube.entity.AdsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsListRepository extends JpaRepository<AdsList, Long> {
}
