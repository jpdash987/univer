package com.univer.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 20)
    private String type; // PERCENT | AMOUNT

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    public Long getId() { return id; }
    public String getCode() { return code; }
    public Scholarship setCode(String code) { this.code = code; return this; }
    public String getType() { return type; }
    public Scholarship setType(String type) { this.type = type; return this; }
    public BigDecimal getValue() { return value; }
    public Scholarship setValue(BigDecimal value) { this.value = value; return this; }
}