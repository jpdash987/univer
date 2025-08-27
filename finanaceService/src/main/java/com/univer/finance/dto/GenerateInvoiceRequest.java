package com.univer.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class GenerateInvoiceRequest {
    @NotEmpty
    private List<String> courseIds;

    @NotBlank
    private String category; // e.g., "DOMESTIC", "INTERNATIONAL"

    private String scholarshipCode; // optional

    public List<String> getCourseIds() { return courseIds; }
    public void setCourseIds(List<String> courseIds) { this.courseIds = courseIds; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getScholarshipCode() { return scholarshipCode; }
    public void setScholarshipCode(String scholarshipCode) { this.scholarshipCode = scholarshipCode; }
}