package com.rider.payment.services.paymentMethods;

import com.rider.payment.entities.paymentMethods.PaymentMethod;
import com.rider.payment.entities.paymentMethods.PaymentType;
import com.rider.payment.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class PIXPaymentMethod implements GenericPaymentMethod {
    private final PaymentType paymentType = PaymentType.PIX;

    public PaymentMethod buildPaymentMethod(PaymentMethodRepository paymentMethodRepository) {
        return paymentMethodRepository.findByPaymentType(this.paymentType);
    }
}

