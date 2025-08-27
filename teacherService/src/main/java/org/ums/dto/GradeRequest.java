package org.ums.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class GradeRequest {
    @NotBlank
    private String itemType; // e.g., "ASSIGNMENT" | "QUIZ" | "EXAM"

    @NotBlank
    private String itemId;   // e.g., "HW1", "MIDTERM"

    @NotNull
    private BigDecimal maxScore;

    @Valid
    @NotNull
    private List<GradeEntry> entries;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public List<GradeEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GradeEntry> entries) {
        this.entries = entries;
    }
}