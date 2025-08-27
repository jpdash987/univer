package com.univer.academic.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GradeUploadRequest {
    @Valid
    @NotNull
    private List<GradeEntryRequest> entries;

    public List<GradeEntryRequest> getEntries() { return entries; }
    public void setEntries(List<GradeEntryRequest> entries) { this.entries = entries; }
}