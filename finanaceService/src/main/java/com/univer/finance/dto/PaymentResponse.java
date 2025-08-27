package com.univer.finance.dto;

public class PaymentResponse {
    private Long paymentId;
    private Long invoiceId;
    private String status;
    private String receiptUrl;

    public Long getPaymentId() { return paymentId; }
    public PaymentResponse setPaymentId(Long paymentId) { this.paymentId = paymentId; return this; }
    public Long getInvoiceId() { return invoiceId; }
    public PaymentResponse setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; return this; }
    public String getStatus() { return status; }
    public PaymentResponse setStatus(String status) { this.status = status; return this; }
    public String getReceiptUrl() { return receiptUrl; }
    public PaymentResponse setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
}