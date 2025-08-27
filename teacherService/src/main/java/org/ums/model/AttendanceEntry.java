package org.ums.model;

import jakarta.persistence.*;

@Entity
@Table(name = "attendance_entries",
       uniqueConstraints = @UniqueConstraint(name = "uq_attendance_record_student", columnNames = {"record_id", "student_id"}))
public class AttendanceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "record_id", nullable = false)
    private AttendanceRecord record;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Boolean present;

    @Column(length = 500)
    private String notes;

    public Long getId() {
        return id;
    }

    public AttendanceRecord getRecord() {
        return record;
    }

    public AttendanceEntry setRecord(AttendanceRecord record) {
        this.record = record;
        return this;
    }

    public String getStudentId() {
        return
                studentId;
    }

    public AttendanceEntry setStudentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public Boolean getPresent() {
        return present;
    }

    public AttendanceEntry setPresent(Boolean present) {
        this.present = present;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public AttendanceEntry setNotes(String notes) {
        this.notes = notes;
        return this;
    }
}