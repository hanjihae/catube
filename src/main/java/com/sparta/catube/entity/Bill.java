package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bill")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private BigDecimal billVideoAmount;
    private BigDecimal billAdAmount;
    private BigDecimal billTotalAmount;

    @ManyToOne
    @JoinColumn(name = "billUserId")
    private User user;
}
