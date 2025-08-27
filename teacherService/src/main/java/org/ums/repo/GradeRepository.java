package org.ums.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.Grade;
import org.ums.model.GradeItem;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByGradeItemAndStudentId(GradeItem gradeItem, Long studentId);
}