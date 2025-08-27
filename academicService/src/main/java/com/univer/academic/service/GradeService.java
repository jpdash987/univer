package com.univer.academic.service;

import com.univer.academic.domain.Grade;
import com.univer.academic.dto.GradeEntryRequest;
import com.univer.academic.dto.GradeUploadRequest;
import com.univer.academic.dto.GradeUploadResponse;
import com.univer.academic.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public GradeUploadResponse uploadGrades(GradeUploadRequest request) {
        int saved = 0;
        for (GradeEntryRequest e : request.getEntries()) {
            String letter = letterForMarks(e.getMarks().doubleValue());
            var g = new Grade()
                    .setStudentId(e.getStudentId())
                    .setCourseId(e.getCourseId())
                    .setSemester(e.getSemester())
                    .setMarks(e.getMarks())
                    .setGrade(letter);
            gradeRepository.save(g);
            saved++;
        }
        return new GradeUploadResponse()
                .setTotal(request.getEntries().size())
                .setSaved(saved);
    }

    public static String letterForMarks(double marks) {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }

    public static double pointsForLetter(String letter) {
        return switch (letter) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }
}