package com.univer.academic.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transcripts",
       indexes = @Index(name="idx_transcripts_student", columnList = "student_id"))
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable = false)
    private Long studentId;

    @Column(name="s3_bucket", nullable = false, length = 100)
    private String s3Bucket;

    @Column(name="s3_key", nullable = false, length = 500)
    private String s3Key;

    @Column(precision = 4, scale = 3)
    private BigDecimal gpa;

    @Column(precision = 4, scale = 3)
    private BigDecimal cgpa;

    @Column(name = "generated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime generatedAt;

    @PrePersist
    public void prePersist() {
        if (generatedAt == null) generatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Transcript setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public String getS3Bucket() { return s3Bucket; }
    public Transcript setS3Bucket(String s3Bucket) { this.s3Bucket = s3Bucket; return this; }
    public String getS3Key() { return s3Key; }
    public Transcript setS3Key(String s3Key) { this.s3Key = s3Key; return this; }
    public BigDecimal getGpa() { return gpa; }
    public Transcript setGpa(BigDecimal gpa) { this.gpa = gpa; return this; }
    public BigDecimal getCgpa() { return cgpa; }
    public Transcript setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; return this; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public Transcript setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
}