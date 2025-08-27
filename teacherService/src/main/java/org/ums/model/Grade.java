package org.ums.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "grades",
       uniqueConstraints = @UniqueConstraint(name = "uq_grade_one_per_item", columnNames = {"grade_item_id", "student_id"}))
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grade_item_id", nullable = false)
    private GradeItem gradeItem;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal score;

    @Column(length = 500)
    private String comment;

    @Column(name = "graded_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime gradedAt;

    public Long getId() {
        return id;
    }

    public GradeItem getGradeItem() {
        return gradeItem;
    }

    public Grade setGradeItem(GradeItem gradeItem) {
        this.gradeItem = gradeItem;
        return this;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Grade setStudentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public BigDecimal getScore() {
        return score;
    }

    public Grade setScore(BigDecimal score) {
        this.score = score;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Grade setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public OffsetDateTime getGradedAt() {
        return gradedAt;
    }

    public Grade setGradedAt(OffsetDateTime gradedAt) {
        this.gradedAt = gradedAt;
        return this;
    }
}