package com.sparta.catube.repository;

import com.sparta.catube.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.refreshToken = :refreshToken WHERE u.userId = :userId")
    void saveUserRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    @Query("SELECT u FROM User u WHERE u.refreshToken = :refreshToken")
    Optional<User> findByRefreshToken(@Param("refreshToken") String refreshToken);

    String findRefreshTokenByUserId(Long userId);
}
