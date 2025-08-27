package org.ums.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.ClassEntity;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCodeAndTerm(String code, String term);
}