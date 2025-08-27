package com.univer.admin.repository;

import com.univer.admin.domain.AdmissionApproval;
import com.univer.admin.domain.Decision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionApprovalRepository extends JpaRepository<AdmissionApproval, Long> {
    Optional<AdmissionApproval> findByAdmissionId(Long admissionId);
    Page<AdmissionApproval> findByDecision(Decision decision, Pageable pageable);
}