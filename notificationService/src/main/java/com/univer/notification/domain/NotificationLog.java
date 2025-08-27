package com.univer.notification.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notification")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // EMAIL | SMS | EVENT
    @Column(nullable = false, length = 20)
    private String type;

    @Column(length = 255)
    private String recipient;

    // SENT | FAILED | RECEIVED
    @Column(nullable = false, length = 30)
    private String status;

    @Lob
    private String payload;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public NotificationLog setType(String type) { this.type = type; return this; }
    public String getRecipient() { return recipient; }
    public NotificationLog setRecipient(String recipient) { this.recipient = recipient; return this; }
    public String getStatus() { return status; }
    public NotificationLog setStatus(String status) { this.status = status; return this; }
    public String getPayload() { return payload; }
    public NotificationLog setPayload(String payload) { this.payload = payload; return this; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public NotificationLog setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}