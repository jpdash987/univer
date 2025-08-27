package com.univer.academic.service;

import com.univer.academic.domain.Exam;
import com.univer.academic.dto.CreateExamRequest;
import com.univer.academic.dto.ExamResponse;
import com.univer.academic.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional
    public ExamResponse createExam(CreateExamRequest req) {
        var exam = new Exam()
                .setCourseId(req.getCourseId())
                .setExamDate(req.getDate())
                .setExamType(req.getType());
        exam = examRepository.save(exam);
        return new ExamResponse()
                .setId(exam.getId())
                .setCourseId(exam.getCourseId())
                .setDate(exam.getExamDate().toString())
                .setType(exam.getExamType());
    }
}