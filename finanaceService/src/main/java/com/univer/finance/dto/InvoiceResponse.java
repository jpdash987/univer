package com.univer.finance.dto;

import java.math.BigDecimal;

public class InvoiceResponse {
    private Long invoiceId;
    private Long studentId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String category;
    private String scholarshipCode;

    public Long getInvoiceId() { return invoiceId; }
    public InvoiceResponse setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; return this; }
    public Long getStudentId() { return studentId; }
    public InvoiceResponse setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public BigDecimal getAmount() { return amount; }
    public InvoiceResponse setAmount(BigDecimal amount) { this.amount = amount; return this; }
    public String getCurrency() { return currency; }
    public InvoiceResponse setCurrency(String currency) { this.currency = currency; return this; }
    public String getStatus() { return status; }
    public InvoiceResponse setStatus(String status) { this.status = status; return this; }
    public String getCategory() { return category; }
    public InvoiceResponse setCategory(String category) { this.category = category; return this; }
    public String getScholarshipCode() { return scholarshipCode; }
    public InvoiceResponse setScholarshipCode(String scholarshipCode) { this.scholarshipCode = scholarshipCode; return this; }
}