package com.univer.admin.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "admissions_approval",
       uniqueConstraints = @UniqueConstraint(name = "uq_admission_decision", columnNames = "admission_id"))
public class AdmissionApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admission_id", nullable = false)
    private Long admissionId;

    @Column(name = "approver_user_id", nullable = false)
    private Long approverUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Decision decision;

    @Column(length = 65535) // TEXT
    private String notes;

    @Column(name = "decided_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime decidedAt;

    @PrePersist
    public void prePersist() {
        if (decidedAt == null) decidedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public AdmissionApproval setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
        return this;
    }

    public Long getApproverUserId() {
        return approverUserId;
    }

    public AdmissionApproval setApproverUserId(Long approverUserId) {
        this.approverUserId = approverUserId;
        return this;
    }

    public Decision getDecision() {
        return decision;
    }

    public AdmissionApproval setDecision(Decision decision) {
        this.decision = decision;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public AdmissionApproval setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public OffsetDateTime getDecidedAt() {
        return decidedAt;
    }

    public AdmissionApproval setDecidedAt(OffsetDateTime decidedAt) {
        this.decidedAt = decidedAt;
        return this;
    }
}