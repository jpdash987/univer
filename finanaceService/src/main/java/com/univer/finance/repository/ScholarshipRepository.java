package com.univer.finance.repository;

import com.univer.finance.domain.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    Optional<Scholarship> findByCode(String code);
}