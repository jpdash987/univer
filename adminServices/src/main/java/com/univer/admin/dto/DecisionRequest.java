package com.univer.admin.dto;

import jakarta.validation.constraints.NotNull;

public class DecisionRequest {
    @NotNull
    private Long approverUserId;

    private String notes;

    public Long getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(Long approverUserId) {
        this.approverUserId = approverUserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}