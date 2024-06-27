package com.rider.payment.controllers;

import com.rider.payment.dto.NewPaymentRequest;
import com.rider.payment.dto.NewRefundRequest;
import com.rider.payment.entities.invoice.Invoice;
import com.rider.payment.entities.invoice.InvoiceType;
import com.rider.payment.entities.payment.Payment;
import com.rider.payment.entities.payment.PaymentStatus;
import com.rider.payment.reponses.ApiResponse;
import com.rider.payment.repositories.InvoiceRepository;
import com.rider.payment.repositories.PaymentMethodRepository;
import com.rider.payment.repositories.PaymentRepository;
import com.rider.payment.services.paymentMethods.PaymentMethodFactory;
import com.rider.payment.services.payments.PaymentProcessing;
import com.rider.payment.services.payments.PaymentStatusManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
public class PaymentController {
    @Autowired
    public PaymentRepository paymentRepository;

    @Autowired
    public InvoiceRepository invoiceRepository;

    @Autowired
    public PaymentMethodRepository paymentMethodRepository;

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Object> doPayment(@RequestBody @Valid NewPaymentRequest request) {
        try {
            Payment payment = PaymentProcessing.processPayment(request.getAmount(), request.getUserName(), PaymentMethodFactory.build(request), paymentRepository, paymentMethodRepository);

            if(payment.getPaymentStatus().equals(PaymentStatus.FAILED))
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("Não foi possível concluir o pagamento com sucesso, tente novamente mais tarde"));

            Invoice invoice = new Invoice(null, request.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus(), InvoiceType.PAYMENT, request.getUserName(), request.getRiderName(), payment.getPaymentDate());

            invoiceRepository.save(invoice);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(invoice));
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse("Dados do Cartão de Crédito enviados não são válidos: " + exception.getMessage()));
        }
    }

    @PostMapping("/refund")
    @Transactional
    public ResponseEntity<Object> refundPayment(@RequestBody @Valid NewRefundRequest request) {
        Payment payment = paymentRepository.findPaymentById(request.getPaymentId());

        if(!payment.getPaymentStatus().equals(PaymentStatus.SUCCESSFUL)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse("Não existe um pagamento elegível para reembolso com o ID especificado"));
        }

        PaymentStatusManager manager = new PaymentStatusManager(paymentRepository);
        manager.refundPayment(payment);

        Invoice invoice = new Invoice(null, payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus(), InvoiceType.REFUND, request.getUserName(), request.getRiderName(), payment.getPaymentDate());

        invoiceRepository.save(invoice);

        return ResponseEntity.noContent().build();
    }
}
