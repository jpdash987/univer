package org.ums.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.AttendanceRecord;
import org.ums.model.ClassEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByClazzAndSessionDate(ClassEntity clazz, LocalDate sessionDate);
}