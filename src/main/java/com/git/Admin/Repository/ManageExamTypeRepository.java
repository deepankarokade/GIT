package com.git.Admin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.ManageExamType;

@Repository
public interface ManageExamTypeRepository extends JpaRepository<ManageExamType, Long> {

    Optional<ManageExamType> findByExamTypeId(String examTypeId);

    List<ManageExamType> findByCourse(Course course);

    List<ManageExamType> findByCourseId(Long courseId);

    boolean existsByExamTypeId(String examTypeId);
}
