package com.rider.payment.services.paymentMethods;


import com.rider.payment.entities.paymentMethods.PaymentMethod;
import com.rider.payment.entities.paymentMethods.PaymentType;
import com.rider.payment.repositories.PaymentMethodRepository;

public interface GenericPaymentMethod {
    PaymentType paymentType = null;

    PaymentMethod buildPaymentMethod(PaymentMethodRepository paymentMethodRepository);
}
