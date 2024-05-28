package com.rider.payment.dto;

import com.rider.payment.entities.paymentMethods.PaymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewPaymentRequest {
    @NotNull
    private PaymentType paymentMethod;

    @Min(1)
    private Double amount;

    @NotNull
    private String riderName;

    @NotNull
    private String userName;

    private String cardHolder;

    private String cardNumber;

    private String cardCode;

    private String cardValidationDate;
}
