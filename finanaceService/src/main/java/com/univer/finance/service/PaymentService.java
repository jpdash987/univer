package com.univer.finance.service;

import com.univer.finance.domain.Invoice;
import com.univer.finance.domain.Payment;
import com.univer.finance.dto.PaymentRequest;
import com.univer.finance.dto.PaymentResponse;
import com.univer.finance.repository.InvoiceRepository;
import com.univer.finance.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class PaymentService {

    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;

    @Value("${app.payments.provider:DUMMY}")
    private String provider;

    @Value("${app.payments.receipt-base-url:https://example.com/receipts}")
    private String receiptBaseUrl;

    public PaymentService(InvoiceRepository invoiceRepo, PaymentRepository paymentRepo) {
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public PaymentResponse makePayment(PaymentRequest req) {
        Invoice inv = invoiceRepo.findById(req.getInvoiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        if (!inv.getStudentId().equals(req.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invoice does not belong to the student");
        }
        if (!"PENDING".equalsIgnoreCase(inv.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invoice is not payable (status=" + inv.getStatus() + ")");
        }

        BigDecimal expected = inv.getAmount();
        if (req.getAmount() == null || req.getAmount().compareTo(expected) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment amount must equal invoice amount");
        }

        // Here you'd call Stripe/Razorpay based on req.getProvider()/provider.
        // For now we simulate success and record the reference.
        var payment = new Payment()
                .setStudentId(inv.getStudentId())
                .setInvoiceId(inv.getId())
                .setAmount(req.getAmount())
                .setStatus("COMPLETED")
                .setProvider(req.getProvider() == null ? provider : req.getProvider().toUpperCase())
                .setReference(req.getReference())
                .setReceiptUrl("%s/%d".formatted(receiptBaseUrl, inv.getId()));
        payment = paymentRepo.save(payment);

        inv.setStatus("PAID").setPaidAt(OffsetDateTime.now());
        invoiceRepo.save(inv);

        return new PaymentResponse()
                .setPaymentId(payment.getId())
                .setInvoiceId(inv.getId())
                .setStatus(payment.getStatus())
                .setReceiptUrl(payment.getReceiptUrl());
    }
}