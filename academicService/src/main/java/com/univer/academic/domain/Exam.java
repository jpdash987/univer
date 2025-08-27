package com.univer.academic.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false, length = 100)
    private String courseId;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "exam_type", nullable = false, length = 50)
    private String examType;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public String getCourseId() { return courseId; }
    public Exam setCourseId(String courseId) { this.courseId = courseId; return this; }
    public LocalDate getExamDate() { return examDate; }
    public Exam setExamDate(LocalDate examDate) { this.examDate = examDate; return this; }
    public String getExamType() { return examType; }
    public Exam setExamType(String examType) { this.examType = examType; return this; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Exam setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}