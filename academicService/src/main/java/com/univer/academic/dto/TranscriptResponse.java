package com.univer.academic.dto;

import java.math.BigDecimal;

public class TranscriptResponse {
    private Long studentId;
    private BigDecimal gpa;
    private BigDecimal cgpa;
    private String bucket;
    private String key;
    private String url; // presigned URL

    public Long getStudentId() { return studentId; }
    public TranscriptResponse setStudentId(Long studentId) { this.studentId = studentId; return this; }
    public BigDecimal getGpa() { return gpa; }
    public TranscriptResponse setGpa(BigDecimal gpa) { this.gpa = gpa; return this; }
    public BigDecimal getCgpa() { return cgpa; }
    public TranscriptResponse setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; return this; }
    public String getBucket() { return bucket; }
    public TranscriptResponse setBucket(String bucket) { this.bucket = bucket; return this; }
    public String getKey() { return key; }
    public TranscriptResponse setKey(String key) { this.key = key; return this; }
    public String getUrl() { return url; }
    public TranscriptResponse setUrl(String url) { this.url = url; return this; }
}