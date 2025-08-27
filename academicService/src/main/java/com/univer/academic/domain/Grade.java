package com.univer.academic.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "grades",
       indexes = {
           @Index(name="idx_grades_student", columnList = "student_id"),
           @Index(name="idx_grades_semester", columnList = "semester"),
           @Index(name="idx_grades_course", columnList = "course_id")
       })
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable = false)
    private Long studentId;

    @Column(name="course_id", nullable = false, length = 100)
    private String courseId;

    @Column(nullable = false, length = 50)
    private String semester;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal marks;

    @Column(nullable = false, length = 5)
    private String grade;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Grade setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public String getCourseId() { return courseId; }
    public Grade setCourseId(String courseId) { this.courseId = courseId; return this; }
    public String getSemester() { return semester; }
    public Grade setSemester(String semester) { this.semester = semester; return this; }
    public BigDecimal getMarks() { return marks; }
    public Grade setMarks(BigDecimal marks) { this.marks = marks; return this; }
    public String getGrade() { return grade; }
    public Grade setGrade(String grade) { this.grade = grade; return this; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Grade setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}