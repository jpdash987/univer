package org.ums.web;

import org.ums.dto.AttendanceRequest;
import org.ums.dto.AttendanceResponse;
import org.ums.dto.GradeRequest;
import org.ums.dto.GradeResponse;
import org.ums.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("teacher-service: OK");
    }

    @PostMapping("/classes/{classId}/attendance")
    public ResponseEntity<AttendanceResponse> recordAttendance(
            @PathVariable String classId,
            @Valid @RequestBody AttendanceRequest request
    ) {
        return ResponseEntity.ok(teacherService.recordAttendance(classId, request));
    }

    @PostMapping("/classes/{classId}/grades")
    public ResponseEntity<GradeResponse> recordGrades(
            @PathVariable String classId,
            @Valid @RequestBody GradeRequest request
    ) {
        return ResponseEntity.ok(teacherService.recordGrades(classId, request));
    }
}