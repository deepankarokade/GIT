package com.git.Professor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.git.Professor.Entity.StudyMaterial;
import com.git.Professor.Repository.StudyMaterialRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudyMaterialService {

    @Autowired
    private StudyMaterialRepository repository;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public StudyMaterial uploadMaterial(String title,
            String subject,
            String category,
            String course,
            String facultyId,
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty!");
        }

        try {

            File folder = new File(UPLOAD_DIR);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(folder, fileName);

            file.transferTo(destination);

            StudyMaterial material = new StudyMaterial();
            material.setTitle(title);
            material.setSubject(subject);
            material.setCategory(category);
            material.setCourse(course);
            material.setFacultyId(facultyId);
            material.setFileName(fileName);
            material.setFileType(file.getContentType());
            material.setFileSize(file.getSize());
            material.setFilePath("uploads/" + fileName);

            material.setUploadDate(LocalDateTime.now());

            return repository.save(material);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    public List<StudyMaterial> getAllMaterials() {
        return repository.findAllByOrderByUploadDateDesc();
    }

    public Optional<StudyMaterial> getById(Long id) {
        if (id == null)
            return Optional.empty();
        return repository.findById(id);
    }

    public List<StudyMaterial> getByCategory(String category) {
        return repository.findByCategoryOrderByUploadDateDesc(category);
    }

    public List<StudyMaterial> getByFacultyAndCategory(String facultyId, String category) {
        return repository.findByFacultyIdAndCategoryOrderByUploadDateDesc(facultyId, category);
    }

    public List<StudyMaterial> getByCategoryAndCourse(String category, String course) {
        return repository.findByCategoryAndCourseOrderByUploadDateDesc(category, course);
    }

    public List<StudyMaterial> getByCourse(String course) {
        return repository.findByCourse(course);
    }

    public void deleteMaterial(Long id) {
        if (id == null)
            return;
        Optional<StudyMaterial> optional = repository.findById(id);

        if (optional.isPresent()) {

            StudyMaterial material = optional.get();

            File file = new File(material.getFilePath());
            if (file.exists()) {
                file.delete();
            }

            repository.delete(material);

        } else {
            throw new RuntimeException("Material not found");
        }
    }
}
