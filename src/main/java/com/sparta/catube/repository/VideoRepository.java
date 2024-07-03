package com.sparta.catube.repository;

import com.sparta.catube.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserUserId(Long userId);

    // 1일 동안 조회수가 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) = CURDATE() ORDER BY video_total_views DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalViewsToday();

    // 1주일 동안 조회수가 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) AND DATE(created_at) <= DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY) ORDER BY video_total_views DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalViewsThisWeek();

    // 1달 동안 조회수가 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) >= DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY) AND DATE(created_at) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY), INTERVAL 1 MONTH) ORDER BY video_total_views DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalViewsThisMonth();

    // 1일 동안 총 재생시간이 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) = CURDATE() ORDER BY video_total_playtime DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalPlayTimeToday();

    // 1주일 동안 총 재생시간이 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) AND DATE(created_at) <= DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY) ORDER BY video_total_playtime DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalPlayTimeThisWeek();

    // 1달 동안 총 재생시간이 높은 동영상 TOP 5
    @Query(value = "SELECT * FROM video WHERE DATE(created_at) >= DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY) AND DATE(created_at) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY), INTERVAL 1 MONTH) ORDER BY video_total_playtime DESC LIMIT 5", nativeQuery = true)
    List<Video> findTop5ByTotalPlayTimeThisMonth();

    
}
