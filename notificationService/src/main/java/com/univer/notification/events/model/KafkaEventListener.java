package com.univer.notification.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univer.notification.dto.EmailRequest;
import com.univer.notification.dto.SmsRequest;
import com.univer.notification.events.model.AdmissionApprovedEvent;
import com.univer.notification.events.model.FeePaidEvent;
import com.univer.notification.events.model.GradeUploadedEvent;
import com.univer.notification.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private final ObjectMapper mapper;
    private final NotificationService notifications;

    public KafkaEventListener(ObjectMapper mapper, NotificationService notifications) {
        this.mapper = mapper;
        this.notifications = notifications;
    }

    @KafkaListener(topics = "${app.kafka.topics.admissionApproved:admissions.approved}")
    public void onAdmissionApproved(ConsumerRecord<String, String> record) {
        try {
            var evt = mapper.readValue(record.value(), AdmissionApprovedEvent.class);
            notifications.logEventReceived(evt);
            if (evt.email != null && !evt.email.isBlank()) {
                notifications.sendEmail(new EmailRequest(){{
                    setTo(evt.email);
                    setSubject("Admission Approved");
                    setBody("Your admission (ID %d) has been approved.".formatted(evt.admissionId));
                    setHtml(false);
                }});
            } else if (evt.phone != null && !evt.phone.isBlank()) {
                notifications.sendSms(new SmsRequest(){{
                    setTo(evt.phone);
                    setMessage("Admission approved. Admission ID: " + evt.admissionId);
                }});
            }
        } catch (Exception ignored) { /* log already saved */ }
    }

    @KafkaListener(topics = "${app.kafka.topics.feePaid:finance.fee_paid}")
    public void onFeePaid(ConsumerRecord<String, String> record) {
        try {
            var evt = mapper.readValue(record.value(), FeePaidEvent.class);
            notifications.logEventReceived(evt);
            if (evt.email != null && !evt.email.isBlank()) {
                notifications.sendEmail(new EmailRequest(){{
                    setTo(evt.email);
                    setSubject("Payment Received");
                    setBody("We received your payment of %s %s (invoice %d).".formatted(evt.amount, evt.currency, evt.invoiceId));
                    setHtml(false);
                }});
            } else if (evt.phone != null && !evt.phone.isBlank()) {
                notifications.sendSms(new SmsRequest(){{
                    setTo(evt.phone);
                    setMessage("Payment received: %s %s (invoice %d)".formatted(evt.amount, evt.currency, evt.invoiceId));
                }});
            }
        } catch (Exception ignored) { }
    }

    @KafkaListener(topics = "${app.kafka.topics.gradeUploaded:academic.grade_uploaded}")
    public void onGradeUploaded(ConsumerRecord<String, String> record) {
        try {
            var evt = mapper.readValue(record.value(), GradeUploadedEvent.class);
            notifications.logEventReceived(evt);
            if (evt.email != null && !evt.email.isBlank()) {
                notifications.sendEmail(new EmailRequest(){{
                    setTo(evt.email);
                    setSubject("Grades Updated");
                    setBody("Your grades for %s have been updated.".formatted(evt.semester));
                    setHtml(false);
                }});
            } else if (evt.phone != null && !evt.phone.isBlank()) {
                notifications.sendSms(new SmsRequest(){{
                    setTo(evt.phone);
                    setMessage("Grades updated for %s".formatted(evt.semester));
                }});
            }
        } catch (Exception ignored) { }
    }
}