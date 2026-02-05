package com.git.Admin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Admin.Entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseName(String courseName);

    boolean existsByCourseId(String courseId);

    Optional<Course> findByCourseId(String courseId);

    List<Course> findByStudentClass(String studentClass);
}
