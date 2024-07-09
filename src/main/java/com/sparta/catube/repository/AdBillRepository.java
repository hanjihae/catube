package com.sparta.catube.repository;

import com.sparta.catube.entity.AdBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdBillRepository extends JpaRepository<AdBill, Long> {
}
