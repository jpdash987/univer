package com.univer.notification.dto;

public class NotificationResponse {
    private Long id;
    private String status;

    public Long getId() { return id; }
    public NotificationResponse setId(Long id) { this.id = id; return this; }
    public String getStatus() { return status; }
    public NotificationResponse setStatus(String status) { this.status = status; return this; }
}