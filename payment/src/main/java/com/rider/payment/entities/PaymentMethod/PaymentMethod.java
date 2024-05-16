package com.rider.payment.entities.PaymentMethod;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table

public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private PaymentType paymentType;

}