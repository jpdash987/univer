package org.ums.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.ums.dto.*;

import org.ums.model.*;
import org.ums.model.AttendanceEntry;
import org.ums.repo.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class TeacherService {

    private static final String DEFAULT_TERM = "DEFAULT";
    private static final long DEFAULT_TEACHER_USER_ID = 0L;

    private final ClassRepository classRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceEntryRepository attendanceEntryRepository;
    private final GradeItemRepository gradeItemRepository;
    private final GradeRepository gradeRepository;

    public TeacherService(ClassRepository classRepository,
                          AttendanceRecordRepository attendanceRecordRepository,
                          AttendanceEntryRepository attendanceEntryRepository,
                          GradeItemRepository gradeItemRepository,
                          GradeRepository gradeRepository) {
        this.classRepository = classRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.attendanceEntryRepository = attendanceEntryRepository;
        this.gradeItemRepository = gradeItemRepository;
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public AttendanceResponse recordAttendance(String classCode, AttendanceRequest request) {
        if (request == null || request.getDate() == null || request.getEntries() == null || request.getEntries().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date and entries are required");
        }

        ClassEntity clazz = getOrCreateClass(classCode);

        var record = attendanceRecordRepository
                .findByClazzAndSessionDate(clazz, request.getDate())
                .orElseGet(() -> attendanceRecordRepository.save(
                        new AttendanceRecord()
                                .setClazz(clazz)
                                .setSessionDate(request.getDate())
                                .setCreatedAt(OffsetDateTime.now())
                ));

        int total = 0;
        int present = 0;

        for (org.ums.dto.AttendanceEntry e : request.getEntries().stream().filter(Objects::nonNull).toList()) {
            Long studentId = parseStudentId(e.getStudentId());
            var entry = attendanceEntryRepository
                    .findByRecordAndStudentId(record, studentId)
                    .orElseGet(() -> new org.ums.model.AttendanceEntry()
                            .setRecord(record)
                            .setStudentId(studentId));

            entry.setPresent(Boolean.TRUE.equals(e.getPresent()));
            entry.setNotes(e.getNotes());
            attendanceEntryRepository.save(entry);

            total++;
            if (Boolean.TRUE.equals(e.getPresent())) present++;
        }

        var resp = new AttendanceResponse();
        resp.setClassId(classCode);
        resp.setDate(request.getDate().toString());
        resp.setTotal(total);
        resp.setPresentCount(present);
        resp.setAbsentCount(total - present);
        return resp;
    }

    @Transactional
    public GradeResponse recordGrades(String classCode, GradeRequest request) {
        if (request == null || request.getItemType() == null || request.getItemId() == null
                || request.getMaxScore() == null || request.getEntries() == null || request.getEntries().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemType, itemId, maxScore and entries are required");
        }

        ClassEntity clazz = getOrCreateClass(classCode);

        var item = gradeItemRepository
                .findByClazzAndItemTypeAndItemId(clazz, request.getItemType(), request.getItemId())
                .orElseGet(() -> gradeItemRepository.save(
                        new GradeItem()
                                .setClazz(clazz)
                                .setItemType(request.getItemType())
                                .setItemId(request.getItemId())
                                .setMaxScore(request.getMaxScore())
                                .setAssignedAt(OffsetDateTime.now())
                ));

        int count = 0;
        for (GradeEntry e : request.getEntries()) {
            Long studentId = parseStudentId(e.getStudentId());
            var grade = gradeRepository
                    .findByGradeItemAndStudentId(item, studentId)
                    .orElseGet(() -> new Grade()
                            .setGradeItem(item)
                            .setStudentId(studentId));

            grade.setScore(e.getScore());
            grade.setComment(e.getComment());
            grade.setGradedAt(OffsetDateTime.now());
            gradeRepository.save(grade);
            count++;
        }

        var resp = new GradeResponse();
        resp.setClassId(classCode);
        resp.setItemType(request.getItemType());
        resp.setItemId(request.getItemId());
        resp.setMaxScore(request.getMaxScore());
        resp.setCount(count);
        return resp;
    }

    private ClassEntity getOrCreateClass(String classCode) {
        return classRepository.findByCodeAndTerm(classCode, DEFAULT_TERM)
                .orElseGet(() -> classRepository.save(
                        new ClassEntity()
                                .setCode(classCode)
                                .setName(classCode)
                                .setTerm(DEFAULT_TERM)
                                .setTeacherUserId(DEFAULT_TEACHER_USER_ID)
                ));
    }

    private Long parseStudentId(String studentId) {
        try {
            return Long.parseLong(studentId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studentId must be a number: " + studentId);
        }
    }
}