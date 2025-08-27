package org.ums.repo;




import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.AttendanceEntry;
import org.ums.model.AttendanceRecord;

import java.util.Optional;

public interface AttendanceEntryRepository extends JpaRepository<AttendanceEntry, Long> {
    Optional<AttendanceEntry> findByRecordAndStudentId(AttendanceRecord record, Long studentId);
}