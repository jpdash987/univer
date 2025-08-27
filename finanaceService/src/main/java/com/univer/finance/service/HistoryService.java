package com.univer.finance.service;

import com.univer.finance.dto.HistoryResponse;
import com.univer.finance.dto.InvoiceResponse;
import com.univer.finance.repository.InvoiceRepository;
import com.univer.finance.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
public class HistoryService {

    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;

    public HistoryService(InvoiceRepository invoiceRepo, PaymentRepository paymentRepo) {
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional(readOnly = true)
    public HistoryResponse history(Long studentId) {
        var dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        var invoices = invoiceRepo.findByStudentId(studentId).stream()
                .map(inv -> new InvoiceResponse()
                        .setInvoiceId(inv.getId())
                        .setStudentId(inv.getStudentId())
                        .setAmount(inv.getAmount())
                        .setCurrency(inv.getCurrency())
                        .setStatus(inv.getStatus())
                        .setCategory(inv.getCategory())
                        .setScholarshipCode(inv.getScholarshipCode()))
                .toList();

        var payments = paymentRepo.findByStudentId(studentId).stream()
                .map(p -> {
                    var s = new HistoryResponse.PaymentSummary();
                    s.id = p.getId();
                    s.invoiceId = p.getInvoiceId();
                    s.amount = p.getAmount().toPlainString();
                    s.status = p.getStatus();
                    s.paidAt = p.getPaidAt() == null ? null : p.getPaidAt().format(dtf);
                    return s;
                })
                .toList();

        return new HistoryResponse()
                .setStudentId(studentId)
                .setInvoices(invoices)
                .setPayments(payments);
    }
}