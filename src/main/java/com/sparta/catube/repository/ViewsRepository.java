package com.sparta.catube.repository;

import com.sparta.catube.entity.User;
import com.sparta.catube.entity.Video;
import com.sparta.catube.entity.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Long> {

    @Query("SELECT v FROM Views v WHERE v.user = :user AND v.video = :video ORDER BY v.createdAt DESC")
    Optional<Views> findLatestViewByUserAndVideo(@Param("user") User user, @Param("video") Video video);

    @Query("SELECT v FROM Views v WHERE v.user = :user AND v.video = :video ORDER BY v.createdAt DESC LIMIT 2")
    List<Views> find2ViewsByUserAndVideo(@Param("user") User user, @Param("video") Video video);

    List<Views> findByVideo(Video video);

//    @Query("SELECT v.video, COUNT(v), SUM(v.viewsPlayTime) FROM Views v WHERE v.user <> :user GROUP BY v.video")
    @Query("SELECT v.video, COUNT(v), SUM(v.viewsPlayTime) FROM Views v GROUP BY v.video")
    List<Object[]> countViewsByVideoExcludingUserGroupByVideo(@Param("user") User user);

}
