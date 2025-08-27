package org.ums.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.List;

public class AttendanceRequest {
    @NotNull
    private LocalDate date;

    @Valid
    @NotNull
    private List<AttendanceEntry> entries;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<AttendanceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<AttendanceEntry> entries) {
        this.entries = entries;
    }
}