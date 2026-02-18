package com.git.Admin.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.ManageExamType;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.ManageExamTypeRepository;

@Service
public class ManageExamTypeService {

	@Autowired
	private ManageExamTypeRepository examTypeRepository;

	@Autowired
	private CourseRepository courseRepository;

	// GET All Exam Types
	public List<ManageExamType> getAllExamTypes() {
		return examTypeRepository.findAll();
	}

	// GET Exam Type by ID
	public ManageExamType getExamTypeById(Long id) {
		return examTypeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Exam Type not found with id: " + id));
	}

	// GET Exam Type by ExamTypeId
	public ManageExamType getExamTypeByExamTypeId(String examTypeId) {
		return examTypeRepository.findByExamTypeId(examTypeId)
				.orElseThrow(() -> new RuntimeException("Exam Type not found with examTypeId: " + examTypeId));
	}

	// GET Exam Types by Course ID
	public List<ManageExamType> getExamTypesByCourseId(Long courseId) {
		return examTypeRepository.findByCourseId(courseId);
	}

	// CREATE Exam Type
	public ManageExamType createExamType(ManageExamType examType) {
		examType.setExamTypeId(generateExamTypeUid());
		return examTypeRepository.save(examType);
	}

	// CREATE Exam Type with Course ID
	public ManageExamType createExamTypeWithCourse(ManageExamType examType, Long courseId) {
		examType.setExamTypeId(generateExamTypeUid());

		if (courseId != null) {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
			examType.setCourse(course);
		}

		return examTypeRepository.save(examType);
	}

	private String generateExamTypeUid() {
		return examTypeRepository.findTopByOrderByIdDesc()
				.map(last -> {
					String lastUid = last.getExamTypeId();
					if (lastUid != null && lastUid.startsWith("EXAM_")) {
						try {
							int lastNumber = Integer.parseInt(lastUid.substring(5));
							return String.format("EXAM_%04d", lastNumber + 1);
						} catch (NumberFormatException e) {
							return String.format("EXAM_%04d", last.getId() + 1);
						}
					}
					return String.format("EXAM_%04d", last.getId() + 1);
				})
				.orElse("EXAM_0001");
	}

	// UPDATE Exam Type
	public ManageExamType updateExamType(Long id, ManageExamType updatedExamType) {
		ManageExamType existing = examTypeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Exam Type not found with id: " + id));

		existing.setExamName(updatedExamType.getExamName());
		existing.setExamDescription(updatedExamType.getExamDescription());

		if (updatedExamType.getCourse() != null) {
			existing.setCourse(updatedExamType.getCourse());
		}

		return examTypeRepository.save(existing);
	}

	// UPDATE Exam Type with Course ID
	public ManageExamType updateExamTypeWithCourse(Long id, ManageExamType updatedExamType, Long courseId) {
		ManageExamType existing = examTypeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Exam Type not found with id: " + id));

		existing.setExamName(updatedExamType.getExamName());
		existing.setExamDescription(updatedExamType.getExamDescription());

		if (courseId != null) {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
			existing.setCourse(course);
		} else {
			existing.setCourse(null);
		}

		return examTypeRepository.save(existing);
	}

	// DELETE Exam Type
	public void deleteExamType(Long id) {
		if (!examTypeRepository.existsById(id)) {
			throw new RuntimeException("Exam Type not found with id: " + id);
		}
		examTypeRepository.deleteById(id);
	}

	// DELETE Exam Type by ExamTypeId
	public void deleteExamTypeByExamTypeId(String examTypeId) {
		ManageExamType examType = examTypeRepository.findByExamTypeId(examTypeId)
				.orElseThrow(() -> new RuntimeException("Exam Type not found with examTypeId: " + examTypeId));
		examTypeRepository.delete(examType);
	}

	// COUNT Total Exam Types
	public long countExamTypes() {
		return examTypeRepository.count();
	}
}
