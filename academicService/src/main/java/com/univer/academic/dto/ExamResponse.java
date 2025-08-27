package com.univer.academic.dto;

public class ExamResponse {
    private Long id;
    private String courseId;
    private String date;
    private String type;

    public Long getId() { return id; }
    public ExamResponse setId(Long id) { this.id = id; return this; }
    public String getCourseId() { return courseId; }
    public ExamResponse setCourseId(String courseId) { this.courseId = courseId; return this; }
    public String getDate() { return date; }
    public ExamResponse setDate(String date) { this.date = date; return this; }
    public String getType() { return type; }
    public ExamResponse setType(String type) { this.type = type; return this; }
}