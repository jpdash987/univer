package com.univer.academic.controller;

import com.univer.academic.dto.*;
import com.univer.academic.service.ExamService;
import com.univer.academic.service.GradeService;
import com.univer.academic.service.TranscriptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academic")
public class AcademicController {

    private final ExamService examService;
    private final GradeService gradeService;
    private final TranscriptService transcriptService;

    public AcademicController(ExamService examService, GradeService gradeService, TranscriptService transcriptService) {
        this.examService = examService;
        this.gradeService = gradeService;
        this.transcriptService = transcriptService;
    }

    // POST /academic/exams (create exam)
    @PostMapping("/exams")
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody CreateExamRequest request) {
        return ResponseEntity.ok(examService.createExam(request));
    }

    // POST /academic/grades (upload grades)
    @PostMapping("/grades")
    public ResponseEntity<GradeUploadResponse> uploadGrades(@Valid @RequestBody GradeUploadRequest request) {
        return ResponseEntity.ok(gradeService.uploadGrades(request));
    }

    // GET /academic/{studentId}/transcript
    @GetMapping("/{studentId}/transcript")
    public ResponseEntity<TranscriptResponse> transcript(@PathVariable Long studentId) {
        return ResponseEntity.ok(transcriptService.generateTranscript(studentId));
    }
}