package com.git.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.git.Admin.Entity.Section;
import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.StudentClass;
import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findBySectionName(String sectionName);

    boolean existsBySectionName(String sectionName);

    Optional<Section> findBySectionId(String sectionId);

    List<Section> findByStudentClass(StudentClass studentClass);

    void deleteByStudentClass(StudentClass studentClass);

    Optional<Section> findTopByOrderByIdDesc();

    List<Section> findByCourse(Course course);
}
