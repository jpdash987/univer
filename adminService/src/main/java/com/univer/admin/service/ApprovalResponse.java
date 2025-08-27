package com.univer.admin.dto;

import com.univer.admin.domain.Decision;

public class ApprovalResponse {
    private Long id;
    private Long admissionId;
    private Long approverUserId;
    private Decision decision;
    private String notes;
    private String decidedAt;

    public Long getId() {
        return id;
    }

    public ApprovalResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public ApprovalResponse setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
        return this;
    }

    public Long getApproverUserId() {
        return approverUserId;
    }

    public ApprovalResponse setApproverUserId(Long approverUserId) {
        this.approverUserId = approverUserId;
        return this;
    }

    public Decision getDecision() {
        return decision;
    }

    public ApprovalResponse setDecision(Decision decision) {
        this.decision = decision;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public ApprovalResponse setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public String getDecidedAt() {
        return decidedAt;
    }

    public ApprovalResponse setDecidedAt(String decidedAt) {
        this.decidedAt = decidedAt;
        return this;
    }
}