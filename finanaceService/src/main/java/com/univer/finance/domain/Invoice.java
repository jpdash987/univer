package com.univer.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "invoices",
       indexes = {
           @Index(name="idx_invoices_student", columnList = "student_id"),
           @Index(name="idx_invoices_status", columnList = "status")
       })
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name="scholarship_code", length = 50)
    private String scholarshipCode;

    @Column(nullable = false, length = 30)
    private String status; // PENDING | PAID | OVERDUE | CANCELLED

    @Column(name="created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @Column(name="paid_at")
    private OffsetDateTime paidAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (status == null) status = "PENDING";
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Invoice setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public BigDecimal getAmount() { return amount; }
    public Invoice setAmount(BigDecimal amount) { this.amount = amount; return this; }
    public String getCurrency() { return currency; }
    public Invoice setCurrency(String currency) { this.currency = currency; return this; }
    public String getCategory() { return category; }
    public Invoice setCategory(String category) { this.category = category; return this; }
    public String getScholarshipCode() { return scholarshipCode; }
    public Invoice setScholarshipCode(String scholarshipCode) { this.scholarshipCode = scholarshipCode; return this; }
    public String getStatus() { return status; }
    public Invoice setStatus(String status) { this.status = status; return this; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Invoice setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
    public OffsetDateTime getPaidAt() { return paidAt; }
    public Invoice setPaidAt(OffsetDateTime paidAt) { this.paidAt = paidAt; return this; }
}