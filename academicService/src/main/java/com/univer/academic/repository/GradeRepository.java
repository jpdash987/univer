package com.univer.academic.repository;

import com.univer.academic.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByStudentIdAndSemester(Long studentId, String semester);
}