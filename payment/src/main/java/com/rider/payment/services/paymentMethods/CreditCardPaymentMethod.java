package com.rider.payment.services.paymentMethods;


import com.rider.payment.entities.paymentMethods.PaymentMethod;
import com.rider.payment.entities.paymentMethods.PaymentType;
import com.rider.payment.repositories.PaymentMethodRepository;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCardPaymentMethod implements GenericPaymentMethod {
    private final PaymentType paymentType = PaymentType.CREDIT_CARD;

    private final String cardNumber;

    private final String cardHolder;

    private final String cardValidationDate;

    private final String cardCode;

    public CreditCardPaymentMethod(String cardNumber, String cardHolder, String cardValidationDate, String cardCode) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.cardValidationDate = cardValidationDate;
        this.cardCode = cardCode;
    }

    public PaymentMethod buildPaymentMethod(PaymentMethodRepository paymentMethodRepository) {
        this.validateCreditCard();

        return paymentMethodRepository.findByPaymentType(this.paymentType);
    }

    private void validateCreditCard() throws IllegalArgumentException {
        // https://stackoverflow.com/a/10575676
        Assert.isTrue(this.cardNumber.length() == 16 && this.cardNumber.matches("[0-9]+"), "Número do Cartão inválido");

        Assert.isTrue(this.cardHolder != null && !this.cardHolder.isBlank(), "Nome do Dono do Cartão precisa existir");

        // https://regex101.com/r/FUfpLq/1
        // https://stackoverflow.com/a/32436019
        Pattern pattern = Pattern.compile("^(0?[1-9]|1[012])/[0-9]{1,2}$");
        Matcher matcher = pattern.matcher(this.cardValidationDate);

        Assert.isTrue(this.cardValidationDate.length() == 5 && matcher.find(), "Data de Validade do Cartão precisa ser válida");

        Assert.isTrue(this.cardCode.length() == 3  && this.cardNumber.matches("[0-9]+"), "Código do Cartão inválido");
    }


}
