package com.univer.admin.controller;

import com.univer.admin.dto.ApprovalResponse;
import com.univer.admin.dto.DecisionRequest;
import com.univer.admin.service.AdminDecisionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminDecisionService service;

    public AdminController(AdminDecisionService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("admin-service: OK");
    }

    @PostMapping("/admissions/{admissionId}/approve")
    public ResponseEntity<ApprovalResponse> approve(
            @PathVariable Long admissionId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return ResponseEntity.ok(service.approve(admissionId, request));
    }

    @PostMapping("/admissions/{admissionId}/reject")
    public ResponseEntity<ApprovalResponse> reject(
            @PathVariable Long admissionId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return ResponseEntity.ok(service.reject(admissionId, request));
    }

    @GetMapping("/admissions/{admissionId}/decision")
    public ResponseEntity<ApprovalResponse> getDecision(@PathVariable Long admissionId) {
        return ResponseEntity.ok(service.getDecision(admissionId));
    }

    @GetMapping("/approvals")
    public ResponseEntity<Page<ApprovalResponse>> list(
            @RequestParam(value = "decision", required = false) String decision,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.list(decision, pageable));
    }
}