package com.univer.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GradeEntryRequest {
    @NotNull
    private Long studentId;

    @NotBlank
    private String courseId;

    @NotBlank
    private String semester;

    @NotNull
    private BigDecimal marks;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public BigDecimal getMarks() { return marks; }
    public void setMarks(BigDecimal marks) { this.marks = marks; }
}