package com.git.Professor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.git.Professor.Entity.StudyMaterial;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {

    List<StudyMaterial> findByCourse(String course);

    List<StudyMaterial> findByFacultyId(String facultyId);

    List<StudyMaterial> findByCategoryAndCourseOrderByUploadDateDesc(String category, String course);

    List<StudyMaterial> findByFacultyIdAndCategoryOrderByUploadDateDesc(String facultyId, String category);

    List<StudyMaterial> findByCategoryOrderByUploadDateDesc(String category);

    List<StudyMaterial> findAllByOrderByUploadDateDesc();
}
