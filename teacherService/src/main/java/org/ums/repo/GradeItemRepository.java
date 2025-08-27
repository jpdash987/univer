package org.ums.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ums.model.ClassEntity;
import org.ums.model.GradeItem;

import java.util.Optional;

public interface GradeItemRepository extends JpaRepository<GradeItem, Long> {
    Optional<GradeItem> findByClazzAndItemTypeAndItemId(ClassEntity clazz, String itemType, String itemId);
}