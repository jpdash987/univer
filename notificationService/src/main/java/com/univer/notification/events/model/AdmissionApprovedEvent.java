package com.univer.notification.events.model;

public class AdmissionApprovedEvent {
    public Long admissionId;
    public Long studentId;
    public String email;  // optional
    public String phone;  // optional
    public String notes;
}