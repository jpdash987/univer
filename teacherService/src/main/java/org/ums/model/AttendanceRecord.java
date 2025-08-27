package org.ums.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "attendance_records",
       uniqueConstraints = @UniqueConstraint(name = "uq_attendance_class_date", columnNames = {"class_id", "session_date"}))
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity clazz;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public AttendanceRecord setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        return this;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public AttendanceRecord setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public AttendanceRecord setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}