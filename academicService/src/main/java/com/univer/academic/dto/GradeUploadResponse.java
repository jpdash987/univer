package com.univer.academic.dto;

public class GradeUploadResponse {
    private int total;
    private int saved;

    public int getTotal() { return total; }
    public GradeUploadResponse setTotal(int total) { this.total = total; return this; }
    public int getSaved() { return saved; }
    public GradeUploadResponse setSaved(int saved) { this.saved = saved; return this; }
}