package com.univer.finance.service;

import com.univer.finance.domain.Invoice;
import com.univer.finance.domain.Scholarship;
import com.univer.finance.dto.GenerateInvoiceRequest;
import com.univer.finance.dto.InvoiceResponse;
import com.univer.finance.repository.FeeStructureRepository;
import com.univer.finance.repository.InvoiceRepository;
import com.univer.finance.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InvoiceService {

    private final FeeStructureRepository feeRepo;
    private final ScholarshipRepository scholarshipRepo;
    private final InvoiceRepository invoiceRepo;

    @Value("${app.currency:USD}")
    private String currency;

    public InvoiceService(FeeStructureRepository feeRepo,
                          ScholarshipRepository scholarshipRepo,
                          InvoiceRepository invoiceRepo) {
        this.feeRepo = feeRepo;
        this.scholarshipRepo = scholarshipRepo;
        this.invoiceRepo = invoiceRepo;
    }

    @Transactional
    public InvoiceResponse generateInvoice(Long studentId, GenerateInvoiceRequest req) {
        if (studentId == null || studentId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid studentId");
        }

        var fees = feeRepo.findByCourseIdInAndCategory(req.getCourseIds(), req.getCategory());
        if (fees.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fee structure for given courses/category");
        }

        BigDecimal total = fees.stream()
                .map(f -> f.getAmount() == null ? BigDecimal.ZERO : f.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String scholarshipCode = req.getScholarshipCode();
        if (scholarshipCode != null && !scholarshipCode.isBlank()) {
            Scholarship sch = scholarshipRepo.findByCode(scholarshipCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid scholarship code"));
            if ("PERCENT".equalsIgnoreCase(sch.getType())) {
                BigDecimal discount = total.multiply(sch.getValue().movePointLeft(2)); // value as percent
                total = total.subtract(discount);
            } else if ("AMOUNT".equalsIgnoreCase(sch.getType())) {
                total = total.subtract(sch.getValue());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown scholarship type");
            }
        }

        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
        total = total.setScale(2, RoundingMode.HALF_UP);

        var inv = new Invoice()
                .setStudentId(studentId)
                .setAmount(total)
                .setCurrency(currency)
                .setCategory(req.getCategory())
                .setScholarshipCode(scholarshipCode)
                .setStatus("PENDING");
        inv = invoiceRepo.save(inv);

        return new InvoiceResponse()
                .setInvoiceId(inv.getId())
                .setStudentId(inv.getStudentId())
                .setAmount(inv.getAmount())
                .setCurrency(inv.getCurrency())
                .setStatus(inv.getStatus())
                .setCategory(inv.getCategory())
                .setScholarshipCode(inv.getScholarshipCode());
    }
}