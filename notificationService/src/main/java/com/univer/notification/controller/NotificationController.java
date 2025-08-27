package com.univer.notification.controller;

import com.univer.notification.dto.EmailRequest;
import com.univer.notification.dto.NotificationResponse;
import com.univer.notification.dto.SmsRequest;
import com.univer.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    private final NotificationService notifications;

    public NotificationController(NotificationService notifications) {
        this.notifications = notifications;
    }

    @GetMapping("/api/notifications/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("notification-service: OK");
    }

    // POST /notifications/email
    @PostMapping("/notifications/email")
    public ResponseEntity<NotificationResponse> email(@Valid @RequestBody EmailRequest request) {
        return ResponseEntity.ok(notifications.sendEmail(request));
    }

    // POST /notifications/sms
    @PostMapping("/notifications/sms")
    public ResponseEntity<NotificationResponse> sms(@Valid @RequestBody SmsRequest request) {
        return ResponseEntity.ok(notifications.sendSms(request));
    }
}