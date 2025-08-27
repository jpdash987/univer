package org.ums.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.Admission;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}