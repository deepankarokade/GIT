package com.git.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.git.Admin.Entity.StudentClass;
import java.util.Optional;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    Optional<StudentClass> findByClassName(String className);

    boolean existsByClassName(String className);

    Optional<StudentClass> findByClassId(String classId);

    Optional<StudentClass> findTopByOrderByIdDesc();
}
