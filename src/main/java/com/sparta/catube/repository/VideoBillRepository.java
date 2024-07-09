package com.sparta.catube.repository;

import com.sparta.catube.entity.VideoBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VideoBillRepository extends JpaRepository<VideoBill, Long> {


    List<VideoBill> findByCreatedAt(LocalDate today);
}
