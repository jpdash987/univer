package com.univer.notification.events;

import com.univer.notification.dto.EmailRequest;
import com.univer.notification.dto.SmsRequest;
import com.univer.notification.events.model.AdmissionApprovedEvent;
import com.univer.notification.events.model.FeePaidEvent;
import com.univer.notification.events.model.GradeUploadedEvent;
import com.univer.notification.service.NotificationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SqsEventListener {

    private final NotificationService notifications;

    public SqsEventListener(NotificationService notifications) {
        this.notifications = notifications;
    }

    @SqsListener("${app.sqs.queues.admissionApproved:admissions-approved-queue}")
    public void onAdmissionApproved(@Payload AdmissionApprovedEvent evt) {
        notifications.logEventReceived(evt);
        if (evt.email != null && !evt.email.isBlank()) {
            var req = new EmailRequest();
            req.setTo(evt.email);
            req.setSubject("Admission Approved");
            req.setBody("Your admission (ID %d) has been approved.".formatted(evt.admissionId));
            notifications.sendEmail(req);
        } else if (evt.phone != null && !evt.phone.isBlank()) {
            var sms = new SmsRequest();
            sms.setTo(evt.phone);
            sms.setMessage("Admission approved. Admission ID: " + evt.admissionId);
            notifications.sendSms(sms);
        }
    }

    @SqsListener("${app.sqs.queues.feePaid:fee-paid-queue}")
    public void onFeePaid(@Payload FeePaidEvent evt) {
        notifications.logEventReceived(evt);
        if (evt.email != null && !evt.email.isBlank()) {
            var req = new EmailRequest();
            req.setTo(evt.email);
            req.setSubject("Payment Received");
            req.setBody("We received your payment of %s %s (invoice %d).".formatted(evt.amount, evt.currency, evt.invoiceId));
            notifications.sendEmail(req);
        } else if (evt.phone != null && !evt.phone.isBlank()) {
            var sms = new SmsRequest();
            sms.setTo(evt.phone);
            sms.setMessage("Payment received: %s %s (invoice %d)".formatted(evt.amount, evt.currency, evt.invoiceId));
            notifications.sendSms(sms);
        }
    }

    @SqsListener("${app.sqs.queues.gradeUploaded:grade-uploaded-queue}")
    public void onGradeUploaded(@Payload GradeUploadedEvent evt) {
        notifications.logEventReceived(evt);
        if (evt.email != null && !evt.email.isBlank()) {
            var req = new EmailRequest();
            req.setTo(evt.email);
            req.setSubject("Grades Updated");
            req.setBody("Your grades for %s have been updated.".formatted(evt.semester));
            notifications.sendEmail(req);
        } else if (evt.phone != null && !evt.phone.isBlank()) {
            var sms = new SmsRequest();
            sms.setTo(evt.phone);
            sms.setMessage("Grades updated for %s".formatted(evt.semester));
            notifications.sendSms(sms);
        }
    }
}