package com.univer.finance.dto;

import java.util.List;

public class HistoryResponse {
    private Long studentId;
    private List<InvoiceResponse> invoices;
    private List<PaymentSummary> payments;

    public static class PaymentSummary {
        public Long id;
        public Long invoiceId;
        public String amount;
        public String status;
        public String paidAt;
    }

    public Long getStudentId() { return studentId; }
    public HistoryResponse setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public List<InvoiceResponse> getInvoices() { return invoices; }
    public HistoryResponse setInvoices(List<InvoiceResponse> invoices) { this.invoices = invoices; return this; }
    public List<PaymentSummary> getPayments() { return payments; }
    public HistoryResponse setPayments(List<PaymentSummary> payments) { this.payments = payments; return this; }
}