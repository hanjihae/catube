package com.sparta.catube.repository;

import com.sparta.catube.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE DATE(b.createdAt) = CURRENT_DATE AND b.user.userId = :userId")
    List<Bill> findBillsCreatedTodayForUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bill b WHERE WEEK(b.createdAt) = WEEK(CURRENT_DATE) AND YEAR(b.createdAt) = YEAR(CURRENT_DATE) AND b.user.userId = :userId")
    List<Bill> findBillsCreatedThisWeekForUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bill b WHERE MONTH(b.createdAt) = MONTH(CURRENT_DATE) AND YEAR(b.createdAt) = YEAR(CURRENT_DATE) AND b.user.userId = :userId")
    List<Bill> findBillsCreatedThisMonthForUser(@Param("userId") Long userId);

}
