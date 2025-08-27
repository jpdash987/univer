package com.univer.finance.controller;

import com.univer.finance.dto.GenerateInvoiceRequest;
import com.univer.finance.dto.InvoiceResponse;
import com.univer.finance.dto.PaymentRequest;
import com.univer.finance.dto.PaymentResponse;
import com.univer.finance.dto.HistoryResponse;
import com.univer.finance.service.HistoryService;
import com.univer.finance.service.InvoiceService;
import com.univer.finance.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final HistoryService historyService;

    public FinanceController(InvoiceService invoiceService,
                             PaymentService paymentService,
                             HistoryService historyService) {
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.historyService = historyService;
    }

    // POST /finance/invoices/{studentId} (generate fee invoice)
    @PostMapping("/invoices/{studentId}")
    public ResponseEntity<InvoiceResponse> generateInvoice(
            @PathVariable Long studentId,
            @Valid @RequestBody GenerateInvoiceRequest request
    ) {
        return ResponseEntity.ok(invoiceService.generateInvoice(studentId, request));
    }

    // POST /finance/payments (make payment)
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> makePayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.makePayment(request));
    }

    // GET /finance/{studentId}/history
    @GetMapping("/{studentId}/history")
    public ResponseEntity<HistoryResponse> history(@PathVariable Long studentId) {
        return ResponseEntity.ok(historyService.history(studentId));
    }
}