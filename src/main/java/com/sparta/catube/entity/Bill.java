package com.sparta.catube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
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
