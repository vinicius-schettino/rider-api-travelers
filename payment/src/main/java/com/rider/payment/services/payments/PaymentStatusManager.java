package com.rider.payment.services.payments;

import com.rider.payment.entities.payment.Payment;
import com.rider.payment.entities.payment.PaymentStatus;
import com.rider.payment.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class PaymentStatusManager {
    private final PaymentRepository paymentRepository;

    public PaymentStatusManager(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
    }

    public void acceptPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);

        paymentRepository.save(payment);
    }

    public void refusePayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.FAILED);

        paymentRepository.save(payment);
    }

    public void refundPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        paymentRepository.save(payment);
    }
}
