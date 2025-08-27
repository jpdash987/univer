package com.univer.notification.events.model;

import java.math.BigDecimal;

public class FeePaidEvent {
    public Long invoiceId;
    public Long studentId;
    public BigDecimal amount;
    public String currency;
    public String email; // optional
    public String phone; // optional
}