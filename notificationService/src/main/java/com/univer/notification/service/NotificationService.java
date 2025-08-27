package com.univer.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univer.notification.domain.NotificationLog;
import com.univer.notification.dto.EmailRequest;
import com.univer.notification.dto.NotificationResponse;
import com.univer.notification.dto.SmsRequest;
import com.univer.notification.repository.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class NotificationService {

    private final SesClient ses;
    private final SnsClient sns;
    private final NotificationLogRepository repo;
    private final ObjectMapper objectMapper;

    @Value("${app.ses.from-address:}")
    private String fromAddress;

    @Value("${app.sns.sms.sender-id:UMS}")
    private String smsSenderId;

    public NotificationService(SesClient ses, SnsClient sns, NotificationLogRepository repo, ObjectMapper objectMapper) {
        this.ses = ses;
        this.sns = sns;
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public NotificationResponse sendEmail(EmailRequest req) {
        if (fromAddress == null || fromAddress.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SES_FROM_ADDRESS not configured");
        }

        try {
            Body body = req.isHtml()
                    ? Body.builder().html(Content.builder().data(req.getBody()).charset("UTF-8").build()).build()
                    : Body.builder().text(Content.builder().data(req.getBody()).charset("UTF-8").build()).build();

            Message message = Message.builder()
                    .subject(Content.builder().data(req.getSubject()).charset("UTF-8").build())
                    .body(body)
                    .build();

            SendEmailRequest emailReq = SendEmailRequest.builder()
                    .source(fromAddress)
                    .destination(Destination.builder().toAddresses(req.getTo()).build())
                    .message(message)
                    .build();

            ses.sendEmail(emailReq);

            var log = repo.save(new NotificationLog()
                    .setType("EMAIL")
                    .setRecipient(req.getTo())
                    .setStatus("SENT")
                    .setPayload(safeJson(req)));
            return new NotificationResponse().setId(log.getId()).setStatus("SENT");
        } catch (SesException e) {
            var log = repo.save(new NotificationLog()
                    .setType("EMAIL")
                    .setRecipient(req.getTo())
                    .setStatus("FAILED")
                    .setPayload(("error=" + e.awsErrorDetails().errorMessage())));
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "SES error: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Transactional
    public NotificationResponse sendSms(SmsRequest req) {
        try {
            PublishRequest pub = PublishRequest.builder()
                    .phoneNumber(req.getTo())
                    .message(req.getMessage())
                    .messageAttributes(Map.of(
                            "AWS.SNS.SMS.SenderID", MessageAttributeValue.builder().stringValue(smsSenderId).dataType("String").build(),
                            "AWS.SNS.SMS.SMSType", MessageAttributeValue.builder().stringValue("Transactional").dataType("String").build()
                    ))
                    .build();

            sns.publish(pub);

            var log = repo.save(new NotificationLog()
                    .setType("SMS")
                    .setRecipient(req.getTo())
                    .setStatus("SENT")
                    .setPayload(safeJson(req)));
            return new NotificationResponse().setId(log.getId()).setStatus("SENT");
        } catch (SnsException e) {
            var log = repo.save(new NotificationLog()
                    .setType("SMS")
                    .setRecipient(req.getTo())
                    .setStatus("FAILED")
                    .setPayload(("error=" + e.awsErrorDetails().errorMessage())));
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "SNS error: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void logEventReceived(Object event) {
        repo.save(new NotificationLog()
                .setType("EVENT")
                .setRecipient(null)
                .setStatus("RECEIVED")
                .setPayload(safeJson(event)));
    }

    private String safeJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception ex) {
            return String.valueOf(o);
        }
    }
}