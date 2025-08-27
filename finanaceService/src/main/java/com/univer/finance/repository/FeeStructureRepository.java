package com.univer.finance.repository;

import com.univer.finance.domain.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    List<FeeStructure> findByCourseIdInAndCategory(Iterable<String> courseIds, String category);
}