package com.univer.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long invoiceId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String provider; // DUMMY | STRIPE | RAZORPAY

    @NotBlank
    private String reference; // token, payment intent id, or transaction ref

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}