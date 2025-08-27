package org.ums.web;

import com.university.student.model.Admission;
import com.university.student.service.AdmissionService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final AdmissionService admissionService;

    public StudentController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    public record ApplyAdmissionRequest(@NotBlank String program){}
    @PostMapping("/admissions/apply")
    public Admission applyAdmission(@RequestBody ApplyAdmissionRequest req, Authentication auth) {
        String username = auth.getName();
        return admissionService.apply(username, req.program());
    }
}