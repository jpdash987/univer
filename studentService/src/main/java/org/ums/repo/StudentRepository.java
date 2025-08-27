package org.ums.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserName(String userName);
}