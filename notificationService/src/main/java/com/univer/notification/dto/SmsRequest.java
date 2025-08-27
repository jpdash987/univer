package com.univer.notification.dto;

import jakarta.validation.constraints.NotBlank;

public class SmsRequest {
    // E.164 format, e.g. +15551234567
    @NotBlank
    private String to;

    @NotBlank
    private String message;

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}