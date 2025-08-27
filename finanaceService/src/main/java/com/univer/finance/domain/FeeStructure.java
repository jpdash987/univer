package com.univer.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_structure",
       uniqueConstraints = @UniqueConstraint(name = "uq_fee_course_category", columnNames = {"course_id", "category"}))
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="course_id", nullable = false, length = 100)
    private String courseId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 50)
    private String category;

    public Long getId() { return id; }
    public String getCourseId() { return courseId; }
    public FeeStructure setCourseId(String courseId) { this.courseId = courseId; return this; }
    public BigDecimal getAmount() { return amount; }
    public FeeStructure setAmount(BigDecimal amount) { this.amount = amount; return this; }
    public String getCategory() { return category; }
    public FeeStructure setCategory(String category) { this.category = category; return this; }
}