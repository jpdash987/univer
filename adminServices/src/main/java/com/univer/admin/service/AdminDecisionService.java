package com.univer.admin.service;

import com.univer.admin.domain.AdmissionApproval;
import com.univer.admin.domain.Decision;
import com.univer.admin.dto.ApprovalResponse;
import com.univer.admin.dto.DecisionRequest;
import com.univer.admin.repository.AdmissionApprovalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;

@Service
public class AdminDecisionService {

    private final AdmissionApprovalRepository repository;

    public AdminDecisionService(AdmissionApprovalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ApprovalResponse approve(Long admissionId, DecisionRequest request) {
        return decide(admissionId, request, Decision.APPROVED);
    }

    @Transactional
    public ApprovalResponse reject(Long admissionId, DecisionRequest request) {
        return decide(admissionId, request, Decision.REJECTED);
    }

    @Transactional(readOnly = true)
    public ApprovalResponse getDecision(Long admissionId) {
        var approval = repository.findByAdmissionId(admissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No decision found for admission " + admissionId));
        return toResponse(approval);
    }

    @Transactional(readOnly = true)
    public Page<ApprovalResponse> list(String decision, Pageable pageable) {
        if (decision == null || decision.isBlank()) {
            return repository.findAll(pageable).map(this::toResponse);
        }
        Decision d;
        try {
            d = Decision.valueOf(decision.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "decision must be APPROVED or REJECTED");
        }
        return repository.findByDecision(d, pageable).map(this::toResponse);
    }

    private ApprovalResponse decide(Long admissionId, DecisionRequest request, Decision decision) {
        if (admissionId == null || admissionId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid admissionId");
        }
        if (request == null || request.getApproverUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "approverUserId is required");
        }

        repository.findByAdmissionId(admissionId).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admission already decided (" + existing.getDecision() + ")");
        });

        var approval = new AdmissionApproval()
                .setAdmissionId(admissionId)
                .setApproverUserId(request.getApproverUserId())
                .setDecision(decision)
                .setNotes(request.getNotes());

        approval = repository.save(approval);
        return toResponse(approval);
    }

    private ApprovalResponse toResponse(AdmissionApproval a) {
        return new ApprovalResponse()
                .setId(a.getId())
                .setAdmissionId(a.getAdmissionId())
                .setApproverUserId(a.getApproverUserId())
                .setDecision(a.getDecision())
                .setNotes(a.getNotes())
                .setDecidedAt(a.getDecidedAt() == null ? null : a.getDecidedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}