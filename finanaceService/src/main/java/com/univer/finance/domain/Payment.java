package com.univer.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments",
       indexes = {
           @Index(name="idx_payments_invoice", columnList = "invoice_id"),
           @Index(name="idx_payments_student", columnList = "student_id")
       })
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable = false)
    private Long studentId;

    @Column(name="invoice_id", nullable = false)
    private Long invoiceId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String status; // COMPLETED | FAILED | REFUNDED

    @Column(length = 50)
    private String provider; // DUMMY | STRIPE | RAZORPAY

    @Column(length = 100)
    private String reference;

    @Column(name="receipt_url", length = 500)
    private String receiptUrl;

    @Column(name="paid_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime paidAt;

    @PrePersist
    public void prePersist() {
        if (paidAt == null) paidAt = OffsetDateTime.now();
        if (status == null) status = "COMPLETED";
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Payment setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public Long getInvoiceId() { return invoiceId; }
    public Payment setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; return this; }
    public BigDecimal getAmount() { return amount; }
    public Payment setAmount(BigDecimal amount) { this.amount = amount; return this; }
    public String getStatus() { return status; }
    public Payment setStatus(String status) { this.status = status; return this; }
    public String getProvider() { return provider; }
    public Payment setProvider(String provider) { this.provider = provider; return this; }
    public String getReference() { return reference; }
    public Payment setReference(String reference) { this.reference = reference; return this; }
    public String getReceiptUrl() { return receiptUrl; }
    public Payment setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
    public OffsetDateTime getPaidAt() { return paidAt; }
    public Payment setPaidAt(OffsetDateTime paidAt) { this.paidAt = paidAt; return this; }
}