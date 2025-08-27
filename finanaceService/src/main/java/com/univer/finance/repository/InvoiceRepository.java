package com.univer.finance.repository;

import com.univer.finance.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStudentId(Long studentId);
    Optional<Invoice> findByIdAndStudentId(Long id, Long studentId);
}