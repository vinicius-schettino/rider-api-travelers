package com.rider.payment.services.payments;

import com.rider.payment.entities.payment.Payment;
import com.rider.payment.entities.payment.PaymentStatus;
import com.rider.payment.services.paymentMethods.GenericPaymentMethod;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Random;

public class PaymentProcessing {
    public static Payment processPayment(Double amount, GenericPaymentMethod paymentMethod) {
        Payment payment = new Payment(amount, paymentMethod.buildPaymentMethod(), PaymentStatus.PENDING, new Date());

        PaymentStatusManager paymentStatusManager = new PaymentStatusManager();

        paymentStatusManager.createPayment(payment);

        try {
            doPayment();

            paymentStatusManager.acceptPayment(payment);
        } catch (IllegalStateException exception) {
            paymentStatusManager.refusePayment(payment);
        }

        return payment;
    }

    private static void doPayment() {
        Random random = new Random();

        int value = random.nextInt(100) + 1;

        // Simulando um processamento demorado da API
        freeze();

        Assert.state(value >= 95, "Pagamento falhou");
    }

    private static void freeze() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

