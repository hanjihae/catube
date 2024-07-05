package com.sparta.catube.entity;

import com.sparta.catube.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private long billVideoAmount;
    private long billAdAmount;
    private long billTotalAmount;
    private Long videoId;

    @ManyToOne
    @JoinColumn(name = "billUserId")
    private User user;

    public static Bill of(long billVideoAmount, long billAdAmount, long billTotalAmount, Long videoId, User user) {
        return Bill.builder()
                .billVideoAmount(billVideoAmount)
                .billAdAmount(billAdAmount)
                .billTotalAmount(billTotalAmount)
                .videoId(videoId)
                .user(user)
                .build();
    }
}
